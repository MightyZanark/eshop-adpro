package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @TestConfiguration
    static class ProductServiceImplTestContextConfiguration {

        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }
    }

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {}

    @Test
    void testFindAll() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        products.add(new Product());
        products.add(null);
        
        Mockito.when(productRepository.findAll()).thenReturn(products.iterator());
        List<Product> allProducts = productService.findAll();
        assertEquals(3, allProducts.size());
        for (int i = 0; i < allProducts.size(); i++) {
            assertTrue(products.get(i) == allProducts.get(i));
        }
    }

    @Test
    void testServiceCreateGiveProductId() {
        Product product = new Product();
        product.setProductName("Test Product 1");
        product.setProductQuantity(10);

        assertNull(product.getProductId());
        product = productService.create(product);
        assertNotNull(product.getProductId());
    }

    @Test
    void testServiceEditAndFind() {
        Product product = new Product();
        product.setProductName("Test Product 1");
        product.setProductQuantity(10);

        Product editProduct = new Product();
        editProduct.setProductName("Test Product 2");
        editProduct.setProductQuantity(20);
        editProduct.setProductId(product.getProductId());

        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                product.setProductName(editProduct.getProductName());
                product.setProductQuantity(editProduct.getProductQuantity());
                return null;
            }

        }).when(productRepository).edit(editProduct);

        Mockito.when(productRepository.find(product.getProductId())).thenReturn(product);

        assertEquals("Test Product 1", product.getProductName());
        productService.edit(editProduct);
        Product editedProduct = productService.find(product.getProductId());
        assertEquals(product.getProductId(), editedProduct.getProductId());
        assertEquals("Test Product 2", editedProduct.getProductName());
        assertEquals(20, editedProduct.getProductQuantity());
    }

    @Test
    void testServiceDelete() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product 1");
        product.setProductQuantity(10);

        Mockito.when(productRepository.delete(product.getProductId())).thenReturn(product);
        Mockito.when(productRepository.find(product.getProductId())).thenThrow(NoSuchElementException.class);

        Product deletedProduct = productService.delete(product.getProductId());
        assertEquals(product.getProductId(), deletedProduct.getProductId());
        assertEquals(product.getProductName(), deletedProduct.getProductName());
        assertEquals(product.getProductQuantity(), deletedProduct.getProductQuantity());
        assertThrows(NoSuchElementException.class, () -> productService.find(deletedProduct.getProductId()));
    }
}
