package db.test.app.product.validation;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import db.test.app.product.Product;

/**
 * Simple product validator.
 */
@Service
public class ProductValidator {

    @Autowired
    private Validator validator;

    /**
     * Validates product.
     */
    public void validate(Product product) {

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            throw new ProductValidationException(
                    violations.stream().map(ConstraintViolation::getMessageTemplate)
                            .collect(Collectors.joining(", ")));
        }
    }
}
