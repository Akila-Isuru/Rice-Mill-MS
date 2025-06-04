package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employeedto {
    private String employeeId;
    private String name;
    private String address;
    private String contactNumber;
    private String jobRole;
    private BigDecimal basicSalary;

    public Employeedto(String employeeId) {
        this.employeeId = employeeId;
    }
}