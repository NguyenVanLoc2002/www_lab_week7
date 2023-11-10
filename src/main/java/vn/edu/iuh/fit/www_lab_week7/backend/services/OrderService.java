package vn.edu.iuh.fit.www_lab_week7.backend.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Employee;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Order;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.OrderRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> findOrdersByOrderDate(LocalDate date){
        return entityManager.createQuery("SELECT o from  Order o where DATE(o.orderDate) =:date ", Order.class)
                .setParameter("date", date)
                .getResultList();
    }

    public List<Employee> findEmpIdByBetweenOrderDate(LocalDate startDate, LocalDate endDate){
        return  entityManager.createQuery("select e " +
                "from Order o join Employee e on o.employee.id = e.id where DATE(o.orderDate) between :startDate and :endDate ", Employee.class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .getResultList();
    }

    public List<Order> findOrdersByEmpIdBetweenOrderDate(LocalDate startDate, LocalDate endDate, Long id){
        return  entityManager.createQuery("select o " +
                        "from Order o where DATE(o.orderDate) between :startDate and :endDate AND o.employee.id = :employeeId", Order.class)
                .setParameter("startDate",startDate)
                .setParameter("endDate",endDate)
                .setParameter("employeeId",id)
                .getResultList();
    }
}
