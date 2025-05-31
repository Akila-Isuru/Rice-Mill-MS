package lk.ijse.gdse74.mytest2.responsive.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString


public class Suppliersdto {
private  String supplierId;
private  String name;
private String contactNumber;
private String address;
private String email;

    public Suppliersdto(String id) {
        this.supplierId = id;
    }
}
