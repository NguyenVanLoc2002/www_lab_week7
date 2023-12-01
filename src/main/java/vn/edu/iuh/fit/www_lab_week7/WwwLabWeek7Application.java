package vn.edu.iuh.fit.www_lab_week7;

import jakarta.persistence.PersistenceContext;
import net.datafaker.Faker;
import net.datafaker.providers.base.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.www_lab_week7.backend.enums.EmployeeStatus;
import vn.edu.iuh.fit.www_lab_week7.backend.enums.ProductStatus;
import vn.edu.iuh.fit.www_lab_week7.backend.models.*;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

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

//    @Bean
    CommandLineRunner creteSampleProductImgage() {
        return args -> {
            List<Product> products = productRepository.findAll();

            if (!products.isEmpty()) {
                Faker faker = new Faker();
                Random rnd = new Random();

                for (Product product : products) {
                    ProductImage productImage = new ProductImage();
                    productImage.setPath(faker.internet().image());
                    productImage.setAlternative(faker.lorem().sentence());
                    productImage.setProduct(product);

                    productImageRepository.save(productImage);
                }
            }
        };
    }


//    @Bean
//    CommandLineRunner creteSampleProductPrice() {
//        return args -> {
//            List<Product> products = productRepository.findAll();
//
//            if (!products.isEmpty()) {
//                Faker faker = new Faker();
//                Random rnd = new Random();
//
//                for (Product product : products) {
//                    LocalDateTime priceDateTime = LocalDateTime.now().minusDays(rnd.nextInt(30));
//                    double price = 10 + rnd.nextDouble() *  (5000 - 10); // Giá ngẫu nhiên từ 10 đến 100
//                    String note = faker.lorem().sentence();
//
//                    ProductPrice productPrice = new ProductPrice();
//                    productPrice.setProduct(product);
//                    productPrice.setPrice_date_time(priceDateTime);
//                    productPrice.setPrice(price);
//                    productPrice.setNote(note);
//
//                    productPriceRepository.save(productPrice);
//                }
//            }
//        };
//    }
}
