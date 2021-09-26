package db.test.app.product.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception indicating that product is not valid.
 */
@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProductValidationException extends RuntimeException {
    public ProductValidationException(String message) {

        super(message);
    }
}
