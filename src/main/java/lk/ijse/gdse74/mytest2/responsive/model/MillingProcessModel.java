package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.dto.MillingProcessdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MillingProcessModel {
    public static ArrayList<MillingProcessdto> viewAllMillingProcess() throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from milling_process");
        ArrayList<MillingProcessdto> List = new ArrayList<>();
        while (rs.next()) {
            MillingProcessdto millingProcessdto = new MillingProcessdto(
                    rs.getString("milling_id"),
                    rs.getString("paddy_id"),
                    rs.getTime("start_time"),
                    rs.getTime("end_time"),
                    rs.getDouble("milled_quantity"),
                    rs.getDouble("broken_rice"),
                    rs.getDouble("husk_kg"),
                    rs.getDouble("bran_kg")
            );
            List.add(millingProcessdto);

        }
        return List;
    }

    public static boolean saveMillingProcess(MillingProcessdto millingProcessdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "insert into milling_process values (?,?,?,?,?,?,?,?)",
                millingProcessdto.getMillingId(),
                millingProcessdto.getPaddyId(),
                millingProcessdto.getStartTime(),
                millingProcessdto.getEndTime(),
                millingProcessdto.getMilledQuantity(),
                millingProcessdto.getBrokenRice(),
                millingProcessdto.getHusk(),
                millingProcessdto.getBran()


        );
    }

    public static boolean deleteMillingProcess(MillingProcessdto millingProcessdto) throws ClassNotFoundException, SQLException {
        String sql = "delete from milling_process where milling_id=?";
        return CrudUtill.execute(sql, millingProcessdto.getMillingId());
    }

    public static boolean updateMillingProcess(MillingProcessdto millingProcessdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "update milling_process set paddy_id =?, start_time =?,end_time = ?,milled_quantity =?,broken_rice =? ,husk_kg =?,bran_kg =? where milling_id = ?",
                millingProcessdto.getPaddyId(),
                millingProcessdto.getStartTime(),
                millingProcessdto.getEndTime(),
                millingProcessdto.getMilledQuantity(),
                millingProcessdto.getBrokenRice(),
                millingProcessdto.getHusk(),
                millingProcessdto.getBran(),
                millingProcessdto.getMillingId()
        );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select milling_id from milling_process order by milling_id desc limit 1");
        char tableChar = 'M'; // Use any character Ex:- customer table for C, item table for I
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

