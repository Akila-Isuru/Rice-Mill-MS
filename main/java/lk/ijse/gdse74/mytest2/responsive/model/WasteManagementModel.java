package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.WasteManagementdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WasteManagementModel {
    public static ArrayList<WasteManagementdto> viewAllWasteManagement() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM waste_management");
        ArrayList<WasteManagementdto> wasteManagement = new ArrayList<>();
        while (rs.next()) {
            wasteManagement.add(new WasteManagementdto(
                    rs.getString("waste_id"),
                    rs.getString("milling_id"),
                    rs.getString("waste_type"),
                    rs.getInt("quantity"),
                    rs.getString("disposal_method"),
                    rs.getDate("recorded_date")
            ));
        }
        return wasteManagement;
    }

    public static String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("SELECT waste_id FROM waste_management ORDER BY waste_id DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString("waste_id");
            int num = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("W%03d", num);
        }
        return "W001";
    }

    public boolean saveWasteManagement(WasteManagementdto wasteManagementdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "INSERT INTO waste_management VALUES (?,?,?,?,?,?)",
                wasteManagementdto.getWasteId(),
                wasteManagementdto.getMillingId(),
                wasteManagementdto.getWasteType(),
                wasteManagementdto.getQuantity(),
                wasteManagementdto.getDisposalMethod(),
                wasteManagementdto.getRecordDate()
        );
    }

    public boolean deleteWasteManagement(WasteManagementdto wasteManagementdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "DELETE FROM waste_management WHERE waste_id=?",
                wasteManagementdto.getWasteId()
        );
    }

    public boolean updateWasteManagement(WasteManagementdto wasteManagementdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "UPDATE waste_management SET milling_id=?, waste_type=?, quantity=?, disposal_method=?, recorded_date=? WHERE waste_id=?",
                wasteManagementdto.getMillingId(),
                wasteManagementdto.getWasteType(),
                wasteManagementdto.getQuantity(),
                wasteManagementdto.getDisposalMethod(),
                wasteManagementdto.getRecordDate(),
                wasteManagementdto.getWasteId()
        );
    }
}