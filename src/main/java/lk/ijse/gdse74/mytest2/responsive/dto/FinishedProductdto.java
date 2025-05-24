package lk.ijse.gdse74.mytest2.responsive.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class FinishedProductdto {
    private String productId;
    private String millingId;
    private String productType;
    private double packageSize;
    private int quantityBags;
    private int pricePerBag;

    public FinishedProductdto(String productId) {
        this.productId = productId;
    }
}
