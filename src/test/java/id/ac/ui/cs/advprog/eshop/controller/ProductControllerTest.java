package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private ProductService service;

    private static Gson gson;

    @BeforeAll
    static void setUp() {
        gson = new Gson();
    }

    @Test
    void testCreateProduct() throws Exception {
        mvc.perform(get("/product/create"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<h3>Create New Product</h3>")))
            .andExpect(content().string(matchesRegex("(?s).*?<label.*?>Name</label>.*?(?-s)")))
            .andExpect(content().string(matchesRegex("(?s).*?<input.*?id=\"nameInput\".*?>.*?(?-s)")))
            .andExpect(content().string(matchesRegex("(?s).*?<label.*?>Quantity</label>.*?(?-s)")))
            .andExpect(content().string(matchesRegex("(?s).*?<input.*?id=\"nameInput\".*?>.*?(?-s)")))
            .andExpect(content().string(matchesRegex("(?s).*?<button type=\"submit\".*?>Submit</button>.*?(?-s)")));
        
        Product product = new Product();
        product.setProductName("Test Product 1");
        product.setProductQuantity(10);

        Mockito.when(service.create(product)).thenReturn(product);

        String productJson = gson.toJson(product, Product.class);

        mvc.perform(post("/product/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productJson))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("list"));
        
        List<Product> products = new ArrayList<>();
        products.add(product);

        Mockito.when(service.findAll()).thenReturn(products);

        mvc.perform(get("/product/list"))
            .andExpect(content().string(containsString(product.getProductName())));
    }

    @Test
    void testEditProduct() throws Exception {
        Product product = new Product();
        product.setProductName("Test Product 1");
        product.setProductQuantity(10);
        product.setProductId("test-id");

        Product editProduct = new Product();
        editProduct.setProductName("Test Product Edit");
        editProduct.setProductQuantity(30);
        editProduct.setProductId(product.getProductId());

        Mockito.doNothing().when(service).edit(editProduct);
        
        Mockito.when(service.find(product.getProductId())).thenReturn(product);

        String editUrl = "/product/edit?id=" + product.getProductId();
        mvc.perform(get(editUrl))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("<h3>Edit Product</h3>")))
            .andExpect(content().string(matchesRegex("(?s).*?<input.*?id=\"nameInput\".*?value=\"" + product.getProductName() + "\">.*?(?-s)")))
            .andExpect(content().string(matchesRegex("(?s).*?<input.*?id=\"quantityInput\".*?value=\"" + product.getProductQuantity() + "\">.*?(?-s)")))
            .andExpect(content().string(matchesRegex("(?s).*?<a.*?onclick=\"submit\\(\\);\">Submit</a>.*?(?-s)")));
        
        String editProductJson = gson.toJson(editProduct, Product.class);
        mvc.perform(put(editUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(editProductJson))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
        List<Product> products = new ArrayList<>();
        products.add(editProduct);
        Mockito.when(service.findAll()).thenReturn(products);

        mvc.perform(get("/product/list"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString(editProduct.getProductName())))
            .andExpect(content().string(containsString(Integer.toString(editProduct.getProductQuantity()))))
            .andExpect(content().string(not(containsString(product.getProductName()))))
            .andExpect(content().string(not(containsString(Integer.toString(product.getProductQuantity())))));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setProductName("Test Product Delete");
        product.setProductQuantity(10);
        product.setProductId("delete-product");

        Mockito.when(service.find(product.getProductId())).thenReturn(product);

        mvc.perform(get("/product/delete?id=" + product.getProductId()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("<h2>Are you sure you want to delete this product?</h2>")))
            .andExpect(content().string(containsString(product.getProductName())))
            .andExpect(content().string(containsString(Integer.toString(product.getProductQuantity()))))
            .andExpect(content().string(matchesRegex("(?s).*?<a.*?onclick=\"deleteRequest\\(\\);\".*?>Delete</a>.*?(?-s)")));
        
        Mockito.when(service.delete(product.getProductId())).thenReturn(product);

        mvc.perform(delete("/product/delete")
            .contentType(MediaType.TEXT_PLAIN)
            .content(product.getProductId()))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
        
        Mockito.when(service.findAll()).thenReturn(new ArrayList<>());

        mvc.perform(get("/product/list"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(not(containsString(product.getProductName()))))
            .andExpect(content().string(not(containsString(Integer.toString(product.getProductQuantity())))));
    }
}
