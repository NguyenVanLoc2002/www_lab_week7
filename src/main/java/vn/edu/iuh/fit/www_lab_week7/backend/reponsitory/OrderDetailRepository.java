package vn.edu.iuh.fit.www_lab_week7.backend.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.models.OrderDetail;
import vn.edu.iuh.fit.www_lab_week7.backend.pks.OrderDetailPK;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPK> {
}