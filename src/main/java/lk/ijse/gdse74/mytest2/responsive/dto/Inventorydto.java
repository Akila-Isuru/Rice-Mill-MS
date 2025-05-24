package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Inventorydto {
    private String id;
    private String productId;
    private int currentStockBags;
    private Date lastUpdated;


    public Inventorydto(String id) {
        this.id = id;
    }
}
