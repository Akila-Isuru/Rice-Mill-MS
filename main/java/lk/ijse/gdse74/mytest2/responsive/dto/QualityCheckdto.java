package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class QualityCheckdto {
    private String checkId;
    private String paddyId;
    private String moistureLevel;
    private String foreignMaterial;
    private String brokenPrecentage;
    private Date inceptionDate;


    public QualityCheckdto(String checkId) {
        this.checkId = checkId;
    }
}
