package db.test.app.product;

import java.time.LocalDate;

/**
 * Static utilities for testing.
 */
public class TestUtils {

    /**
     * Creates product entity.
     */
    public static Product getProduct(int id, String name, double price, LocalDate creationDate,
            boolean deleted) {

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(creationDate);
        if (deleted) {
            product.markAsDeleted();
        }
        return product;
    }
}
