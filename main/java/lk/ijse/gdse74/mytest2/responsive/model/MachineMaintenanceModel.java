package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.MachineMaintenancedto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MachineMaintenanceModel {
    public static ArrayList<MachineMaintenancedto> viewAllMachineMaintenance ()throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from machine_maintenance");
        ArrayList<MachineMaintenancedto> maintenances = new ArrayList<>();
        while (rs.next()) {
            MachineMaintenancedto machineMaintenance = new MachineMaintenancedto(
                    rs.getString("maintenance_id"),
                    rs.getString("machine_name"),
                    rs.getString("maintenance_date"),
                    rs.getString("description"),
                    rs.getInt("cost")
            );
            maintenances.add(machineMaintenance);
        }
        return maintenances;
    }
    public boolean saveMachineMaintenance(MachineMaintenancedto machineMaintenancedto)throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
          "insert into machine_maintenance values (?,?,?,?,?)",
                machineMaintenancedto.getMaintenanceId(),
                machineMaintenancedto.getMachineName(),
                machineMaintenancedto.getMaintenanceDate(),
                machineMaintenancedto.getDescription(),
                machineMaintenancedto.getCost()
        );
    }
public boolean deleteMachineMaintenance(MachineMaintenancedto machineMaintenancedto)throws ClassNotFoundException, SQLException {
        String sql = "delete from machine_maintenance where maintenance_id=?";
        return CrudUtill.execute(sql, machineMaintenancedto.getMaintenanceId());
    }
    public boolean updateMachineMaintenance(MachineMaintenancedto machineMaintenancedto)throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "update machine_maintenance set machine_name =?,maintenance_date =?,description =?,cost =? where maintenance_id=?",
                machineMaintenancedto.getMachineName(),
                machineMaintenancedto.getMaintenanceDate(),
                machineMaintenancedto.getDescription(),
                machineMaintenancedto.getCost(),
                machineMaintenancedto.getMaintenanceId()
        );
    }
}
