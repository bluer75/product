package db.test.app.product.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception indicating that product could not be found.
 */
@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND,
    reason = "Product not found")
public class ProductNotFoundException extends RuntimeException {
}
