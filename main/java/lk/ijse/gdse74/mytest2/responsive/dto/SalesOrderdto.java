package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class SalesOrderdto {
    private String orderId;
    private String customerId;
    private Date orderDate;
    private int orderAmount;
    private ArrayList<SalesOrderDetailsdto> cartList;



    public SalesOrderdto(String id) {
        this.orderId = id;
    }


    public SalesOrderdto(String orderId, String customerId, java.sql.Date orderDate, int totalAmount) {
    }
}
