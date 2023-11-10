package vn.edu.iuh.fit.www_lab_week7.frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.www_lab_week7.backend.enums.ProductStatus;
import vn.edu.iuh.fit.www_lab_week7.backend.models.Product;
import vn.edu.iuh.fit.www_lab_week7.backend.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showListProduct(Model model,
                                  @RequestParam("page")Optional<Integer> page,
                                  @RequestParam("size")Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Product> productPage = productService.findPagingnated(currentPage-1, pageSize, "name", "asc");
        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers= IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/product/listProduct";
    }

    @GetMapping("/addProduct")
    public String showFormAddProduct(Model model){
        Product product = new Product();
        model.addAttribute("product",product);
        ProductStatus[] productStatuses = ProductStatus.values();
        model.addAttribute("productStatuses",productStatuses);
        return "admin/product/addProduct";
    }

    @PostMapping("/addProduct/new")
    public String addNewProduct(@ModelAttribute("product") Product product){
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showFormEditProduct(@PathVariable("id") Long id, Model model){
        Product product = productService.findById(id).get();
        model.addAttribute("product", product);
        ProductStatus[] productStatuses = ProductStatus.values();
        model.addAttribute("productStatuses",productStatuses);
        return "admin/product/editProduct";
    }

    @PostMapping("/editProduct")
    public String editProduct(@ModelAttribute("product") Product product){
        Product existingProduct = productService.findById(product.getProduct_id()).orElse(null);
        if(existingProduct!=null){
            existingProduct.setName(product.getName());
            existingProduct.setManufacturer(product.getManufacturer());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setStatus(product.getStatus());
            existingProduct.setUnit(product.getUnit());
            productService.saveProduct(existingProduct);
        }
        return "redirect:/products";
    }
}
