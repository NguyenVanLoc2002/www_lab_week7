package vn.edu.iuh.fit.www_lab_week7.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.www_lab_week7.backend.models.ProductImage;
import vn.edu.iuh.fit.www_lab_week7.backend.reponsitory.ProductImageRepository;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    public void insertProductDetails(ProductImage productImage){
        productImageRepository.save(productImage);
    }
}
