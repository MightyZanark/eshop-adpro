package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Queue;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();
    private Map<String, Integer> productIdToIdx = new HashMap<>();
    private Queue<Integer> emptyIndexes = new ArrayDeque<>();

    public Product create(Product product) {
        int index;
        if (emptyIndexes.isEmpty()) {
            productData.add(product);
            index = productData.size() - 1;
        } else {
            index = emptyIndexes.poll();
            productData.set(index, product);
        }
        productIdToIdx.put(product.getProductId(), index);
        
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public int getProductIndex(String productId) {
        Integer index = productIdToIdx.get(productId);
        if (index == null) {
            throw new NoSuchElementException("Product with id " + productId + " does not exists");
        }
        return index;
    }

    public Product find(String productId) {
        Integer index = getProductIndex(productId);
        return productData.get(index);
    }

    public void edit(Product product) {
        Integer index = getProductIndex(product.getProductId());
        productData.set(index, product);
    }

    public Product delete(String productId) {
        Integer index = getProductIndex(productId);
        Product product = productData.get(index);
        productData.set(index, null);
        emptyIndexes.add(index);
        productIdToIdx.remove(productId);
        return product;
    }
}
