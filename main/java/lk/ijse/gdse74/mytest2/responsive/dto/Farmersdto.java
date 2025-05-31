package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Farmersdto {
    private String farmerId;
    private String name;
    private String contactNumber;
    private String address;

    public Farmersdto(String id) {

        this.farmerId = id;
    }
}
