package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Reportsdto {
    private String reportId;
    private String reportType;
    private String reportDate;


    public Reportsdto(String id) {
        this.reportId = id;
    }
}
