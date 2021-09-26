package db.test.app.product;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import db.test.app.product.validation.ProductNotFoundException;
import db.test.app.product.validation.ProductValidator;

/**
 * Service that provides operation for product.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductValidator validator;

    /**
     * Gets all non-deleted products.
     */
    public List<Product> getProducts() {

        return repository.findAllNonDeleted();
    }

    /**
     * Gets all deleted products.
     */
    public List<Product> getDeletedProducts() {

        return repository.findAllDeleted();
    }

    /**
     * Get product with given id or null.
     */
    public Product getProduct(int id) {

        return repository.findNonDeletedById(id).orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Add new product.
     * Values for id and creationDate are generated. 
     */
    @Transactional
    public Product addProduct(Product product) {

        validate(product);
        // set id to non-existing value to avoid updating existing entity
        product.setId(-1);
        // use current date for creatioDate
        product.setCreationDate(LocalDate.now());
        return repository.save(product);
    }

    /**
     * Updates product with given id.
     * Only name and price can be updated.
     */
    @Transactional
    public Product updateProduct(int id, Product product) {

        Product originalProduct = repository.findNonDeletedById(id)
                .orElseThrow(ProductNotFoundException::new);
        validate(product);
        // make sure correct id and creationDate is set
        product.setId(id);
        product.setCreationDate(originalProduct.getCreationDate());
        return repository.save(product);
    }

    /**
     * Deletes product with given id.
     * It performs soft deletion by setting the deleted flag to true.
     */
    @Transactional
    public void deleteProduct(int id) {

        Product product = repository.findNonDeletedById(id)
                .orElseThrow(ProductNotFoundException::new);
        product.markAsDeleted();
        repository.softDelete(id);
    }

    private void validate(Product product) {

        validator.validate(product);
    }
}
