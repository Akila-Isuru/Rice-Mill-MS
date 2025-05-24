package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SalesOrderDetailsdto {
    private String orderId;
    private String productId;
    private int unitPrice;
    private int qty;
    private int totalPrice;
}
