package lk.ijse.gdse74.mytest2.responsive.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString



public class Customersdto {
    private String customerId;
    private String name;
    private String contactNumber;
    private String address;
    private String email;

    public Customersdto(String id) {
        this.customerId = id;
    }
}
