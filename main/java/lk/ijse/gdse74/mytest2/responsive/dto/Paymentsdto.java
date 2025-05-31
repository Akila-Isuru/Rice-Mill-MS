package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class Paymentsdto {
    private String paymentId;
    private String orderId;
    private Date paymentDate;
    private String paymentMethods;
    private int amountPaid;

    public Paymentsdto(String id) {
        this.paymentId = id;
    }
}
