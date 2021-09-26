package db.test.app.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Product REST controller.
 */
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(
        value = "Retrieves only non-deleted or only deleted products (see deleted parameter)")
    @GetMapping("/")
    public List<Product> getProducts(@ApiParam(name = "deleted",
        value = "indicates whether only non-deleted (default/false) or only deleted (true) prodcuts should be returned") @RequestParam(
            name = "deleted",
            defaultValue = "false") boolean deleted) {

        return deleted ? productService.getDeletedProducts() : productService.getProducts();
    }

    @ApiOperation(value = "Retrieves product with given id")
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") int id) {

        return productService.getProduct(id);
    }

    @ApiOperation(value = "Creates new product",
        notes = "Values for id and creationDate are generated, these values provided in product are ignored")
    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {

        return productService.addProduct(product);
    }

    @ApiOperation(value = "Updates product with given id",
        notes = "Only name and price can by changed")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") int id, @Valid @RequestBody Product product) {

        return productService.updateProduct(id, product);
    }

    @ApiOperation(value = "Deletes product with given id")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") int id) {

        productService.deleteProduct(id);
    }
}
