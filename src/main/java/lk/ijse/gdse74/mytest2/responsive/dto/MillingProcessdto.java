package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString


public class MillingProcessdto {


    private String millingId;
    private  String paddyId;
    private Time startTime;
    private Time endTime;
    private double milledQuantity;
    private double brokenRice;
    private double husk;
    private double bran;

    public MillingProcessdto(String id) {
        this.millingId = id;
    }
}
