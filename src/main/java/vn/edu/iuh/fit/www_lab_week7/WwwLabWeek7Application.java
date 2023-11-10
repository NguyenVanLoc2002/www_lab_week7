package vn.edu.iuh.fit.www_lab_week7;

import net.datafaker.Faker;
import net.datafaker.providers.base.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.www_lab_week7.backend.enums.EmployeeStatus;
import vn.edu.iuh.fit.www_lab_week7.backend.enums.ProductStatus;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Customer;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Employee;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Order;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Product;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.CustomerRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.EmployeeRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.OrderRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.ProductRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
public class WwwLabWeek7Application {

    public static void main(String[] args) {
        SpringApplication.run(WwwLabWeek7Application.class, args);
    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrderRepository orderRepository;

//    @Bean
    CommandLineRunner createSampleCustomersAndEmployees() {
        return args -> {
            Faker faker = new Faker();
            Random rnd = new Random();

            int desiredPhoneLength = 15; // Độ dài mong muốn cho số điện thoại
            for (int i = 0; i < 200; i++) {
                String phoneNumber = faker.phoneNumber().phoneNumber();

                if (phoneNumber.length() > desiredPhoneLength) {
                    phoneNumber = phoneNumber.substring(0, desiredPhoneLength); // Cắt chuỗi để có độ dài mong muốn
                } else if (phoneNumber.length() < desiredPhoneLength) {
                    // Có thể thêm các ký tự khác để đảm bảo độ dài cố định, ví dụ: thêm các số 0
                    while (phoneNumber.length() < desiredPhoneLength) {
                        phoneNumber += "0";
                    }
                }
                Customer customer = new Customer(
                        faker.name().fullName(),
                        faker.internet().emailAddress(),
                        phoneNumber,
                        faker.address().fullAddress()
                );
                customerRepository.save(customer);

                Employee employee = new Employee(
                        faker.name().fullName(),
                        faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        faker.internet().emailAddress(),
                        phoneNumber,
                        faker.address().fullAddress(),
                        EmployeeStatus.ACTIVE
                );
                employeeRepository.save(employee);


                // Tạo ngày giả dưới dạng java.util.Date
                Date fakeDate = faker.date().birthday();

                // Chuyển đổi ngày java.util.Date thành LocalDateTime
                LocalDateTime orderDate = fakeDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                Order order = new Order(
                        orderDate,
                        employee,
                        customer
                );
                orderRepository.save(order);
            }
        };
    }

//    @Bean
    CommandLineRunner creteSampleProduct() {
        return args -> {
            Faker faker = new Faker();
            Random rnd = new Random();
            Device device = faker.device();
            for (int i = 0; i < 200; i++) {
                Product product = new Product(
                        device.modelName(),
                        faker.lorem().paragraph(30),
                        "piece",
                        device.manufacturer(),
                        ProductStatus.ACTIVE
                );
                productRepository.save(product);
            }
        };
    }
}
