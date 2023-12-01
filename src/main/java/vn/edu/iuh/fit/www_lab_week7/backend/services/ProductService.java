package vn.edu.iuh.fit.www_lab_week7.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.www_lab_week7.backend.dto.CartItem;
import vn.edu.iuh.fit.www_lab_week7.backend.dto.ProductInfo;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Product;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findPagingnated(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return productRepository.findAll(pageable);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<ProductInfo> findAllProductInfoWithLatestPrice() {
        LocalDateTime cur = LocalDateTime.now();
        return productRepository.findAllProductInfoWithLatestPrice(cur);
    }

    public Double calcTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                total += cartItem.getQuantity() * cartItem.getProductInfo().getPrice();
            }
        }
        return total;
    }
}
