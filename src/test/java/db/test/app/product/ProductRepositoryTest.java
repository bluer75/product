package db.test.app.product;

import static db.test.app.product.TestUtils.getProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Test of ProductRepository.
 */
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    /**
     * Tests if unique id is generated.
     */
    @Test
    public void testIdGeneration() {

        int expectedSize = 5;
        Product product = getProduct(-1, "product", 10, LocalDate.now(), false);
        assertEquals(0, repository.findAll().size());
        for (int i = 0; i < expectedSize; i++) {
            repository.save(product);
        }
        // id is primary key so it needs to be unique - check the count of elements
        assertEquals(expectedSize, repository.findAll().size());
    }

    /**
     *  Tests {@link ProductRepository#findAllNonDeleted()}
     */
    @Test
    public void testFindAllNonDeleted() {

        repository.save(getProduct(-1, "product 1", 10, LocalDate.now(), false));
        repository.save(getProduct(-1, "product 2", 10, LocalDate.now(), false));
        // validate both products are accessible
        List<Product> products = repository.findAllNonDeleted();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    /**
     *  Tests {@link ProductRepository#findAllDeleted()}
     */
    @Test
    public void testFindAllDeleted() {

        repository.save(getProduct(-1, "product 1", 10, LocalDate.now(), false));
        repository.save(getProduct(-1, "product 2", 10, LocalDate.now(), true));
        // validate deleted products
        List<Product> products = repository.findAllDeleted();
        assertNotNull(products);
        assertEquals(1, products.size());
    }

    /**
     *  Tests {@link ProductRepository#findNonDeletedById(Integer)}
     */
    @Test
    public void testFindNonDeletedById() {

        Product product = repository.save(getProduct(-1, "product 1", 10, LocalDate.now(), false));
        repository.save(getProduct(-1, "product 2", 10, LocalDate.now(), true));
        // validate non-deleted products can be found
        Optional<Product> nonDeletedproduct = repository.findNonDeletedById(product.getId());
        assertNotNull(nonDeletedproduct.get());
        assertEquals(product, nonDeletedproduct.get());
    }

    /**
     *  Tests {@link ProductRepository#softDelete(Integer)}
     */
    @Test
    public void testSoftDelete() {

        Product product = repository.save(getProduct(-1, "product 1", 10, LocalDate.now(), false));
        // delete and validate
        repository.softDelete(product.getId());
        // product should be still accessible using findAll query
        List<Product> products = repository.findAll();
        assertNotNull(products);
        assertEquals(1, products.size());
        assertTrue(products.get(0).isDeleted());
        // validate attributes didn't changed
        product.markAsDeleted();
        assertEquals(product,
                products.stream().filter(p -> p.getId() == product.getId()).findFirst().get());
    }
}
