package vn.edu.iuh.fit.www_lab_week7.backend.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}