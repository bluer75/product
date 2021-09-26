package db.test.app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import db.test.app.product.ProductController;

/**
 * Test of ProductApplication.
 */
@SpringBootTest
public class ProductApplicationTest {

    @Autowired
    private ProductController controller;

    /**
     * Tests context initialization.
     */
    @Test
    void contextLoads() {

        assertThat(controller).isNotNull();
    }

}
