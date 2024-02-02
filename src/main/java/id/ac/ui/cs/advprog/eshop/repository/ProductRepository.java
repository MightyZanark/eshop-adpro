package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.NoSuchElementException;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();
    private Map<String, Integer> productIdToIdx = new HashMap<>();

    public Product create(Product product) {
        String id = UUID.randomUUID().toString();
        product.setProductId(id);
        productData.add(product);
        productIdToIdx.put(id, productData.size() - 1);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    private int getProductIndex(String productId) {
        Integer index = productIdToIdx.get(productId);
        if (index == null)
            throw new NoSuchElementException("Product with id " + productId + " does not exists");
        return index;
    }

    public Product find(String productId) {
        Integer index = getProductIndex(productId);
        return productData.get(index);
    }

    public void edit(Product newProduct) {
        Integer index = getProductIndex(newProduct.getProductId());
        productData.set(index, newProduct);
    }
}
