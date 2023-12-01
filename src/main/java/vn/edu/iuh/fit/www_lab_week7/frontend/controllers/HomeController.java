package vn.edu.iuh.fit.www_lab_week7.frontend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.iuh.fit.www_lab_week7.backend.dto.ProductInfo;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Product;
import vn.edu.iuh.fit.www_lab_week7.backend.models.ProductImage;
import vn.edu.iuh.fit.www_lab_week7.backend.models.ProductPrice;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.ProductPriceRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.services.ProductImageService;
import vn.edu.iuh.fit.www_lab_week7.backend.services.ProductPriceService;
import vn.edu.iuh.fit.www_lab_week7.backend.services.ProductService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model){
        return "home";
    }


}
