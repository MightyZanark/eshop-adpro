package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {
    
    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {}

    @Test
    void testCreateAndFind() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);
        
        Product product2 = new Product();
        product2.setProductId("a0f9de46-98b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditAndFindOneProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        Product savedProduct = productRepository.create(product);
        
        assertNotEquals("Sampo Cap Usep", savedProduct.getProductName());
        Product editedProduct = new Product();
        editedProduct.setProductId(savedProduct.getProductId());
        editedProduct.setProductName("Sampo Cap Usep");
        editedProduct.setProductQuantity(50);
        productRepository.edit(editedProduct);
        savedProduct = productRepository.find(product.getProductId());
        assertEquals("Sampo Cap Usep", savedProduct.getProductName());
        assertNotEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(50, savedProduct.getProductQuantity());
        assertNotEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
        assertEquals(product.getProductId(), savedProduct.getProductId());
    }

    @Test
    void testDeleteProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);
        
        Product product2 = new Product();
        product2.setProductId("a0f9de46-98b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        assertDoesNotThrow(() -> productRepository.find(product1.getProductId()));
        assertDoesNotThrow(() -> productRepository.find(product2.getProductId()));
        productRepository.delete(product1.getProductId());
        assertThrows(NoSuchElementException.class, () -> productRepository.find(product1.getProductId()));
        assertDoesNotThrow(() -> productRepository.find(product2.getProductId()));
        productRepository.delete(product2.getProductId());
        assertThrows(NoSuchElementException.class, () -> productRepository.find(product2.getProductId()));
    }

    @Test
    void testCreateAfterDelete() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        assertDoesNotThrow(() -> productRepository.find(product1.getProductId()));
        assertEquals(0, productRepository.getProductIndex(product1.getProductId()));
        productRepository.delete(product1.getProductId());
        assertThrows(NoSuchElementException.class, () -> productRepository.find(product1.getProductId()));
        
        Product product2 = new Product();
        product2.setProductId("a0f9de46-98b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);
        
        assertDoesNotThrow(() -> productRepository.find(product2.getProductId()));
        assertEquals(0, productRepository.getProductIndex(product2.getProductId()));
    }
}
