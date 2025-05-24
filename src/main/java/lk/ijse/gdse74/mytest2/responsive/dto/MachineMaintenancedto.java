package lk.ijse.gdse74.mytest2.responsive.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class MachineMaintenancedto {
    private String maintenanceId;
    private String machineName;
    private String maintenanceDate;
    private String description;
    private int cost;


    public MachineMaintenancedto(String id) {
        this.maintenanceId = id;
    }
}
