package db.test.app.product;

import static db.test.app.product.TestUtils.getProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import db.test.app.product.validation.ProductNotFoundException;
import db.test.app.product.validation.ProductValidationException;

/**
 * Test of ProductService.
 * It uses mocked JPA repository.
 */
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService service;

    @MockBean
    private ProductRepository repository;

    private Product[] getProducts() {

        // always create new instances
        return new Product[] { //
                getProduct(1, "product 1", 10.50, LocalDate.now().minusDays(1), false), //
                getProduct(2, "product 2", 20.50, LocalDate.now().minusDays(2), false), //
                getProduct(3, "product 3", 30.50, LocalDate.now().minusDays(3), false), //
                getProduct(4, "product 4", 40.50, LocalDate.now().minusDays(4), false), //
        };
    };

    private Product[] getDeletedProducts() {

        Product[] deletedProducts = getProducts();
        Arrays.stream(deletedProducts).forEach(Product::markAsDeleted);
        return deletedProducts;
    };

    /**
     * Tests {@link ProductService#getProducts()}.
     */
    @Test
    public void testGetProducts() {

        // setup mock
        when(repository.findAllNonDeleted()).thenReturn(asList(getProducts()));
        List<Product> products = service.getProducts();

        // validate all products are returned without modifications
        assertTrue(Arrays.deepEquals(getProducts(), asArray(products)));
    }

    /**
     * Tests {@link ProductService#getDeletedProducts()}.
     */
    @Test
    public void testGetDeletedProducts() {

        // setup mock
        when(repository.findAllDeleted()).thenReturn(asList(getDeletedProducts()));
        List<Product> products = service.getDeletedProducts();

        // validate all products are returned without modifications
        assertTrue(Arrays.deepEquals(getDeletedProducts(), asArray(products)));
    }

    /**
     * Tests {@link ProductService#getProduct(int)}.
     */
    @Test
    public void testGetProduct() {

        // setup mocks
        when(repository.findNonDeletedById(1)).thenReturn(Optional.of(getProducts()[0]));
        when(repository.findNonDeletedById(5)).thenReturn(Optional.empty());

        // validate product is returned without modifications
        assertEquals(getProducts()[0], service.getProduct(1));
        // validate non-existing id is properly handled
        assertThrows(ProductNotFoundException.class, () -> service.getProduct(5));
    }

    /**
     * Tests {@link ProductService#addProduct(Product)}.
     */
    @Test
    public void testAddProduct() {

        int generatedId = 1;
        Product invalidProduct = getProduct(0, null, 0, null, false);
        Product validProduct = getProduct(-1, "product", 10, LocalDate.now().minusDays(10), false);
        Product expectedProduct = getProduct(generatedId, "product", 10, LocalDate.now(), false);

        // setup mock
        when(repository.save(any(Product.class))).thenAnswer(i -> updateId(i, generatedId));

        // check if validation has been triggered
        assertThrows(ProductValidationException.class, () -> service.addProduct(invalidProduct));
        // validate if id and creationDate are updated and name and price didn't change
        Product newProduct = service.addProduct(validProduct);
        assertNotNull(newProduct);
        assertEquals(expectedProduct, newProduct);
    }

    /**
     * Tests {@link ProductService#updateProduct(int, Product)}.
     */
    @Test
    public void testUpdateProduct() {

        Product invalidProduct = getProduct(0, null, 0, null, false);
        Product validProduct = getProduct(-1, "product", 10, LocalDate.now().minusDays(10), false);
        Product originalProduct = getProducts()[0];
        Product expectedProduct = getProduct(originalProduct.getId(), "product", 10,
                originalProduct.getCreationDate(), false);

        // setup mocks
        when(repository.findNonDeletedById(1)).thenReturn(Optional.of(originalProduct));
        when(repository.findNonDeletedById(5)).thenReturn(Optional.empty());
        when(repository.save(any(Product.class))).thenAnswer(returnsFirstArg());

        // validate non-existing id is properly handled
        assertThrows(ProductNotFoundException.class, () -> service.updateProduct(5, validProduct));
        // check if validation has been triggered
        assertThrows(ProductValidationException.class,
                () -> service.updateProduct(1, invalidProduct));
        Product updatedProduct = service.updateProduct(1, validProduct);
        // validate if name and price are updated and id and creationDate didn't change
        assertNotNull(updatedProduct);
        assertEquals(expectedProduct, updatedProduct);
    }

    /**
     * Tests {@link ProductService#deleteProduct(int)}.
     */
    @Test
    public void testDeleteProduct() {

        Product productToBeDeleted = getProducts()[0];
        Product expectedProduct = getProducts()[0];
        expectedProduct.markAsDeleted();

        // setup mocks
        when(repository.findNonDeletedById(1)).thenReturn(Optional.of(productToBeDeleted));
        when(repository.findNonDeletedById(5)).thenReturn(Optional.empty());
        when(repository.save(any(Product.class))).thenAnswer(returnsFirstArg());

        service.deleteProduct(1);
        // validate product is marked as deleted
        assertEquals(expectedProduct, productToBeDeleted);
        // validate non-existing id is properly handled
        assertThrows(ProductNotFoundException.class, () -> service.deleteProduct(5));
    }

    /**
     * Simulates auto generation of id.
     */
    private Product updateId(InvocationOnMock invocation, int generatedId) {

        ((Product) invocation.getArgument(0)).setId(generatedId);
        return invocation.getArgument(0);
    }

    private List<Product> asList(Product... products) {

        return Arrays.stream(products).collect(Collectors.toList());
    }

    private Product[] asArray(List<Product> products) {

        return products.toArray(new Product[products.size()]);
    }
}
