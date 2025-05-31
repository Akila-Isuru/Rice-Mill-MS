package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.MachineMaintenancedto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MachineMaintenanceModel {
    public static ArrayList<MachineMaintenancedto> viewAllMachineMaintenance() throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM machine_maintenance");
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

    public boolean saveMachineMaintenance(MachineMaintenancedto machineMaintenancedto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "INSERT INTO machine_maintenance VALUES (?,?,?,?,?)",
                machineMaintenancedto.getMaintenanceId(),
                machineMaintenancedto.getMachineName(),
                machineMaintenancedto.getMaintenanceDate(),
                machineMaintenancedto.getDescription(),
                machineMaintenancedto.getCost()
        );
    }

    public boolean deleteMachineMaintenance(MachineMaintenancedto machineMaintenancedto) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM machine_maintenance WHERE maintenance_id=?";
        return CrudUtill.execute(sql, machineMaintenancedto.getMaintenanceId());
    }

    public boolean updateMachineMaintenance(MachineMaintenancedto machineMaintenancedto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "UPDATE machine_maintenance SET machine_name=?, maintenance_date=?, description=?, cost=? WHERE maintenance_id=?",
                machineMaintenancedto.getMachineName(),
                machineMaintenancedto.getMaintenanceDate(),
                machineMaintenancedto.getDescription(),
                machineMaintenancedto.getCost(),
                machineMaintenancedto.getMaintenanceId()
        );
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtill.execute("SELECT maintenance_id FROM machine_maintenance ORDER BY maintenance_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            try {
                int lastIdNumber = Integer.parseInt(lastId.substring(2)); // Skip 'MM' and parse to int
                return String.format("MM%03d", lastIdNumber + 1); // Increment and format
            } catch (NumberFormatException e) {
                // If parsing fails, return MM001
                return "MM001";
            }
        }
        return "MM001"; // If no records found, start with MM001
    }
}