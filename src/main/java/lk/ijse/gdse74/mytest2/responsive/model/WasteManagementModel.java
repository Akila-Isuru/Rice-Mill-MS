package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.WasteManagementdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WasteManagementModel {
    public static ArrayList<WasteManagementdto> viewAllWasteManagement() throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM waste_management");
        ArrayList<WasteManagementdto> wasteManagement = new ArrayList<>();
        while (rs.next()) {
            WasteManagementdto wasteManagementdto = new WasteManagementdto(
                    rs.getString("waste_id"),
                    rs.getString("milling_id"),
                    rs.getString("waste_type"),
                    rs.getInt("quantity"),
                    rs.getString("disposal_method"),
                    rs.getDate("recorded_date")
            );
            wasteManagement.add(wasteManagementdto);
        }
        return wasteManagement;
    }
    public boolean saveWasteManagement(WasteManagementdto wasteManagementdto) throws SQLException {
        return CrudUtill.execute(
                "insert into waste_management values (?,?,?,?,?,?)",
                wasteManagementdto.getWasteId(),
                wasteManagementdto.getMillingId(),
                wasteManagementdto.getWasteType(),
                wasteManagementdto.getQuantity(),
                wasteManagementdto.getDisposalMethod(),
                wasteManagementdto.getRecordDate()
        );
    }
    public boolean deleteWasteManagement(WasteManagementdto wasteManagementdto) throws SQLException {
        String sql = "delete from waste_management where waste_id=?";
        return CrudUtill.execute(sql, wasteManagementdto.getWasteId());
    }
    public boolean updateWasteManagement(WasteManagementdto wasteManagementdto) throws SQLException {
        return CrudUtill.execute(
          "update waste_management set milling_id=?,waste_type=?,quantity=?,disposal_method=?,recorded_date=? where waste_id=? "  ,
          wasteManagementdto.getMillingId(),
          wasteManagementdto.getWasteType(),
          wasteManagementdto.getQuantity(),
          wasteManagementdto.getDisposalMethod(),
          wasteManagementdto.getRecordDate(),
          wasteManagementdto.getWasteId()
        );
    }
}
