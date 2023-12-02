package vn.edu.iuh.fit.www_lab_week7.frontend.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.www_lab_week7.backend.dto.CartItem;
import vn.edu.iuh.fit.www_lab_week7.backend.dto.ProductInfo;
import vn.edu.iuh.fit.www_lab_week7.backend.models.ProductPrice;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.ProductPriceRepository;
import vn.edu.iuh.fit.www_lab_week7.backend.services.ProductImageService;
import vn.edu.iuh.fit.www_lab_week7.backend.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ShoppingController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @GetMapping("/shopping")
    public String showShopping(Model model) {
        List<ProductInfo> productInfoList = productService.findAllProductInfoWithLatestPrice();
        List<ProductPrice> productPrices = productPriceRepository.findAll();
        List<ProductInfo> productInfos = productInfoList.stream().collect(Collectors.toMap(ProductInfo::getProductId, Function.identity(), (existing, replacement) -> existing)).values().stream().collect(Collectors.toList());
        // Hiển thị thông tin sử dụng Logger
        logger.info("ProductInfos: {}", productInfos);
        logger.info("ProductPrice: {}", productPrices);

        CartItem cartItem = new CartItem();
        model.addAttribute("productInfos", productInfos);
        model.addAttribute("cartItem", cartItem);
        return "user/shopping";
    }

    @GetMapping("/addToCart")
    public String getCartItem(HttpSession session, @RequestParam Long id,
                              @RequestParam String path, @RequestParam String name,
                              @RequestParam String price,
                              Model model) {
        List<CartItem> list = (List<CartItem>) session.getAttribute("cartItemList");
        if (list == null) {
            list = new ArrayList<>();
        }
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(id);
        productInfo.setImagePath(path);
        productInfo.setName(name);
        productInfo.setPrice(Double.parseDouble(price));

        boolean found = false;

        for (CartItem cartItem1 : list) {
            if (cartItem1.getProductInfo().equals(productInfo)) {
                cartItem1.setQuantity(cartItem1.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            // Nếu không tìm thấy, thêm một CartItem mới vào danh sách
            CartItem cartItem = new CartItem(productInfo, 1);
            list.add(cartItem);
        }

        session.setAttribute("cartItemList", list);
        logger.info("ss: {}", session.getAttribute("cartItemList"));
        return "redirect:/shopping";
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        List<CartItem> list = (List<CartItem>) session.getAttribute("cartItemList");
        Double totalPrice = productService.calcTotalPrice(list);

        model.addAttribute("lstCartItem", list);
        model.addAttribute("total", totalPrice);

        logger.info("ss: {}", list);
        return "user/checkout";
    }

    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        List<CartItem> list = (List<CartItem>) session.getAttribute("cartItemList");
        for (CartItem cartItem1 : list) {
            if (cartItem1.getProductInfo().getProductId().equals(productId)) {
                cartItem1.setQuantity(quantity);
            }
        }
        logger.info("ss: {}", list);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkOut(HttpSession session) {
        session.invalidate();
        return "redirect:/shopping";
    }
}


