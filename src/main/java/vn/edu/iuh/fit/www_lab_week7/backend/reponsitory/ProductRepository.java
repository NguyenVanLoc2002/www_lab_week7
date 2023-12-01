package vn.edu.iuh.fit.www_lab_week7.backend.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.iuh.fit.www_lab_week7.backend.dto.ProductInfo;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT NEW vn.edu.iuh.fit.www_lab_week7.backend.dto.ProductInfo(p.product_id,pi.path, p.name, pp.price) " +
            "FROM ProductImage pi " +
            "JOIN pi.product p " +
            "JOIN p.productPrices pp " +
            "WHERE pp.price_date_time = (" +
            "    SELECT MAX(pp2.price_date_time) " +
            "    FROM ProductPrice pp2 " +
            "    WHERE pp2.price_date_time <= :cur AND pp2.product.product_id = p.product_id)")
    List<ProductInfo> findAllProductInfoWithLatestPrice(@Param("cur") LocalDateTime cur);
}