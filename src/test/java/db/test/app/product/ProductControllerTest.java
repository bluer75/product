package db.test.app.product;

import static db.test.app.product.TestUtils.getProduct;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import db.test.app.product.validation.ProductNotFoundException;
import db.test.app.product.validation.ProductValidationException;

/**
 * Test of ProductController.
 */
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    /**
     * Tests {@link ProductController#getProducts()}. 
     */
    @Test
    public void testGetProducts() throws Exception {

        Product[] nonDeletedProducts = { getProduct(1, "product 1", 10.50, LocalDate.now(), false),
                getProduct(2, "product 2", 20.50, LocalDate.now(), false) };
        Product[] deletedProducts = { getProduct(3, "product 3", 30.50, LocalDate.now(), true),
                getProduct(4, "product 4", 40.50, LocalDate.now(), true) };
        // setup mocks
        when(service.getProducts()).thenReturn(Arrays.asList(nonDeletedProducts));
        when(service.getDeletedProducts()).thenReturn(Arrays.asList(deletedProducts));
        // validate without deleted parameter
        mockMvc.perform(get("/products/"))//
                .andExpect(status().isOk())//
                .andExpect(content().json(asJson(nonDeletedProducts), true));
        // validate with deleted = true
        mockMvc.perform(get("/products/").param("deleted", "true"))//
                .andExpect(status().isOk())//
                .andExpect(content().json(asJson(deletedProducts), true));
        // validate with deleted = false
        mockMvc.perform(get("/products/").param("deleted", "false"))//
                .andExpect(status().isOk())//
                .andExpect(content().json(asJson(nonDeletedProducts), true));
    }

    /**
     * Tests {@link ProductController#getProduct(int)}. 
     */
    @Test
    public void testGetProduct() throws Exception {

        Product product = getProduct(1, "product 1", 10.50, LocalDate.now(), false);
        // setup mocks
        when(service.getProduct(1)).thenReturn(product);
        when(service.getProduct(2)).thenThrow(new ProductNotFoundException());
        when(service.getProduct(3)).thenThrow(new ProductValidationException("validation error"));
        // validate
        mockMvc.perform(get("/products/1"))//
                .andExpect(status().isOk())//
                .andExpect(content().json(asJson(product), true));
        mockMvc.perform(get("/products/2"))//
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/products/3"))//
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests {@link ProductController#addProduct(Product)}. 
     */
    @Test
    public void testAddProduct() throws Exception {

        Product product = getProduct(1, "product 1", 10.50, LocalDate.now(), false);
        // setup mock
        when(service.addProduct(any(Product.class))).thenReturn(product);
        // validate
        mockMvc.perform(
                post("/products/").contentType(MediaType.APPLICATION_JSON).content(asJson(product)))//
                .andExpect(status().isOk())//
                .andExpect(content().json(asJson(product), true));
    }

    /**
     * Tests {@link ProductController#addProduct(Product)}. 
     */
    @Test
    public void testAddInvalidProduct() throws Exception {

        // setup mock
        when(service.addProduct(any(Product.class)))
                .thenThrow(new ProductValidationException("validation error"));
        // validate
        mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
                .content(asJson(new Product())))//
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests {@link ProductController#updateProduct(int, Product)}. 
     */
    @Test
    public void testUpdateProduct() throws Exception {

        Product product = getProduct(1, "product 1", 10.50, LocalDate.now(), false);
        // setup mocks
        when(service.updateProduct(eq(1), any(Product.class))).thenReturn(product);
        when(service.updateProduct(eq(2), any(Product.class)))
                .thenThrow(new ProductNotFoundException());
        // validate
        mockMvc.perform(
                put("/products/1").contentType(MediaType.APPLICATION_JSON).content(asJson(product)))//
                .andExpect(status().isOk())//
                .andExpect(content().json(asJson(product), true));
        mockMvc.perform(
                put("/products/2").contentType(MediaType.APPLICATION_JSON).content(asJson(product)))//
                .andExpect(status().isNotFound());
    }

    /**
     * Tests {@link ProductController#deleteProduct(int)}. 
     */
    @Test
    public void testDeleteProduct() throws Exception {

        doNothing().when(service).deleteProduct(1);
        // setup mock
        doThrow(new ProductNotFoundException()).when(service).deleteProduct(2);
        // validate
        mockMvc.perform(delete("/products/1"))//
                .andExpect(status().isOk())//
                .andExpect(content().string(""));
        mockMvc.perform(delete("/products/2"))//
                .andExpect(status().isNotFound());
    }

    private String asJson(Product product) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(product);
    }

    private String asJson(Product[] products) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(products);
    }
}