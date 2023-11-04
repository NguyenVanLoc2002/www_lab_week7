package vn.edu.iuh.fit.www_lab_week7.backend.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.models.ProductPrice;
import vn.edu.iuh.fit.www_lab_week7.backend.pks.ProductPricePK;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, ProductPricePK> {
}