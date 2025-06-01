package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class RawPaddydto {
    private String paddyId;
    private String supplierId; // Can be null now
    private String farmerId;   // Can be null now
    private double quantity;
    private double moisture;
    private double purchasePrice;
    private Date purchaseDate;


    public RawPaddydto(String paddyId) {
        this.paddyId = paddyId;
    }
}