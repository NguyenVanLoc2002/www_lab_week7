package vn.edu.iuh.fit.www_lab_week7.backend.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.ProductPriceRepository;

import java.time.LocalDateTime;

@Service
public class ProductPriceService {
    @Autowired
    private ProductPriceRepository productPriceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public double getNearestPriceByProductId(long id) {
        LocalDateTime cur = LocalDateTime.now();
        String sql = "SELECT price FROM ProductPrice  e " +
                "    WHERE e.price_date_time = (" +
                "    SELECT MAX(e2.price_date_time)" +
                "       FROM ProductPrice e2 " +
                "     WHERE e2.price_date_time <= :cur AND  e2.product.id=:id)";
        Query query = entityManager.createQuery(sql, Double.class);
        query.setParameter("cur", cur);
        query.setParameter("id", id);
        return (double) query.getSingleResult();
    }
}
