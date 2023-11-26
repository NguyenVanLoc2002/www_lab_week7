package vn.edu.iuh.fit.www_lab_week7.frontend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Employee;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Order;
import vn.edu.iuh.fit.www_lab_week7.backend.services.OrderService;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/ordersByOrderDate")
    public String showFormFindOrderByDate(){
        return "admin/order/listOrderByOrderDate";
    }
    @GetMapping("/orders/date")
    public String showOrdersByDate(HttpServletRequest request, Model model){
        LocalDate orderDate = LocalDate.parse(request.getParameter("orderDate"));
        List<Order> orderList = orderService.findOrdersByOrderDate(orderDate);
        model.addAttribute("orderList",orderList);
        return "admin/order/listOrderByOrderDate";
    }

    @GetMapping("/ordersByEmployeeBetweenOrderDate")
    public String showFormFindOrderByEmployeeBetweenOrderDate(Model model){
        return "admin/order/listOrderByEmpBetweenOrderDate";
    }

    @GetMapping("/orders/emp-and-date")
    public String showEmployeeByBetweenOrderDate(HttpServletRequest request, Model model, HttpSession session){
        LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
        LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
        List<Employee> employees = orderService.findEmpIdByBetweenOrderDate(startDate,endDate);
        model.addAttribute("employees",employees);

        session.setAttribute("startDate",startDate);
        session.setAttribute("endDate",endDate);
        return "admin/order/listOrderByEmpBetweenOrderDate";
    }

    @GetMapping("/orders/emp-by-date/{id}")
    public String showOrdersByEmployeeBetweenOrderDate(@PathVariable("id") Long id, Model model,HttpSession session){
        LocalDate startDate = (LocalDate) session.getAttribute("startDate") ;
        LocalDate endDate = (LocalDate) session.getAttribute("endDate") ;

        List<Order> orderList = orderService.findOrdersByEmpIdBetweenOrderDate(startDate,endDate,id);
        model.addAttribute("orderList",orderList);
        return "admin/order/listDetailOrderByEmpBetweenOrderDate";
    }

}
