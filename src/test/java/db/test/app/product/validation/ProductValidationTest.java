package db.test.app.product.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import db.test.app.product.Product;

/**
 * Test of ProductValidator.
 */
@SpringBootTest
public class ProductValidationTest {

    @Autowired
    private ProductValidator validator;

    /**
     * Tests valid product.
     */
    @Test
    public void testValidProduct() {

        Product product = getProduct("product name", 10.50);
        validator.validate(product);
        // no exception expected
    }

    /**
     * Tests empty product.
     */
    @Test
    public void testEmptyProduct() {

        Product product = new Product();
        Exception exception = assertThrows(ProductValidationException.class,
                () -> validator.validate(product));
        assertThat(exception).hasMessageContaining("name of the product")
                .hasMessageContaining("price of the product");
    }

    /**
     * Tests name validation.
     */
    @Test
    public void testProductName() {

        Product[] products = { getProduct(null, 10.50), getProduct("", 10.50),
                getProduct("   ", 10.50) };
        for (Product product : products) {
            Exception exception = assertThrows(ProductValidationException.class,
                    () -> validator.validate(product));
            assertThat(exception).hasMessageContaining("name of the product");
        }
    }

    /**
     * Tests price validation.
     */
    @Test
    public void testProductWithoutPrice() {

        Product[] products = { getProduct("product", 0), getProduct("product", -10.50) };
        for (Product product : products) {
            Exception exception = assertThrows(ProductValidationException.class,
                    () -> validator.validate(product));
            assertThat(exception).hasMessageContaining("price of the product");
        }
    }

    private Product getProduct(String name, double price) {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
