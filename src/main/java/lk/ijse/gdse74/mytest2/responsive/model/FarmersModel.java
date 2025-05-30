package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FarmersModel {
    public ArrayList<Farmersdto> viewAllFarmers()throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from farmers");
        ArrayList<Farmersdto> farmers = new ArrayList<>();
        while (rs.next()) {
            Farmersdto farmersdto = new Farmersdto(
                    rs.getString("farmer_id"),
                    rs.getString("name"),
                    rs.getString("contact_number"),
                    rs.getString("address")
            );
            farmers.add(farmersdto);
        }
        return farmers;
    }

    public boolean saveFarmer(Farmersdto farmersdto)throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "insert into farmers values (?,?,?,?)",
                farmersdto.getFarmerId(),
                farmersdto.getName(),
                farmersdto.getAddress(),
                farmersdto.getContactNumber()
        );
    }

    public boolean deleteFarmer(Farmersdto farmersdto) throws SQLException {
        String sql = "delete from farmers where farmer_id=?";
        return CrudUtill.execute(sql,farmersdto.getFarmerId());
    }

    public boolean updateFarmer(Farmersdto farmersdto) throws SQLException {
        return CrudUtill.execute(
                "update farmers set name = ?, contact_number = ?,address = ? where farmer_id = ?",
                farmersdto.getName(),
                farmersdto.getAddress(),
                farmersdto.getContactNumber(),
                farmersdto.getFarmerId()
                );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select farmer_id from farmers order by farmer_id desc limit 1");
        char tableChar = 'F'; // Use any character Ex:- customer table for C, item table for I
        if (resultSet.next()) {
            String lastId = resultSet.getString(1); // "C004"
            String lastIdNUmberString = lastId.substring(1); // "004"
            int lastIdNumber = Integer.parseInt(lastIdNUmberString); // 4
            int nextIdNumber = lastIdNumber + 1; // 5
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber); // "C005"
            return nextIdString;
        }
        return tableChar + "001";
    }
    }

