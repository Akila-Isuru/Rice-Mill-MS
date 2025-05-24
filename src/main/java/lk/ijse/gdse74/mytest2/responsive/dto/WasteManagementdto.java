package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class WasteManagementdto {
    private String wasteId;
    private String millingId;
    private String wasteType;
    private int quantity;
    private String disposalMethod;
    private Date recordDate;


    public WasteManagementdto(String wasteId) {
        this.wasteId = wasteId;
    }
}
