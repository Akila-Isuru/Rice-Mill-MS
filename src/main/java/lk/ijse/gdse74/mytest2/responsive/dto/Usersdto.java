package lk.ijse.gdse74.mytest2.responsive.dto;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


public class Usersdto {
    private String user_id;
    private String name;
    private String email;
    private String role;
    private String contact_number;

    public Usersdto(String id) {
        this.user_id = id;
    }
}

