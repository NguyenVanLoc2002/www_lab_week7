package vn.edu.iuh.fit.www_lab_week7.backend.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItem {
    private ProductInfo productInfo;
    private int quantity;
}
