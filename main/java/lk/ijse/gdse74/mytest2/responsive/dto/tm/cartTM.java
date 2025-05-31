package lk.ijse.gdse74.mytest2.responsive.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class cartTM {
private String productId;
private String productName;
private int unitPrice;
private int qty;
private int total;
private Button btnRemove;

}
