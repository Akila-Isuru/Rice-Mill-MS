package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceDto {
    private String attendanceId;
    private String employeeId;
    private LocalDate date;
    private String status;
    private LocalTime inTime;
    private LocalTime outTime;
    private Double hoursWorked; // Using Double for simplicity in JavaFX table, BigDecimal for calculations if needed
}