package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.MillingProcessdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class MillingProcessModel {

    public static ArrayList<MillingProcessdto> viewAllMillingProcess() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM milling_process";
        ResultSet rs = CrudUtill.execute(sql);

        ArrayList<MillingProcessdto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new MillingProcessdto(
                    rs.getString("milling_id"),
                    rs.getString("paddy_id"),
                    rs.getTime("start_time"),
                    rs.getTime("end_time"),
                    rs.getDouble("milled_quantity"),
                    rs.getDouble("broken_rice"),
                    rs.getDouble("husk_kg"),
                    rs.getDouble("bran_kg")
            ));
        }
        return list;
    }

    public static boolean saveMillingProcess(MillingProcessdto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO milling_process VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtill.execute(
                sql,
                dto.getMillingId(),
                dto.getPaddyId(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getMilledQuantity(),
                dto.getBrokenRice(),
                dto.getHusk(),
                dto.getBran()
        );
    }

    public static boolean updateMillingProcess(MillingProcessdto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE milling_process SET paddy_id=?, start_time=?, end_time=?, " +
                "milled_quantity=?, broken_rice=?, husk_kg=?, bran_kg=? " +
                "WHERE milling_id=?";
        return CrudUtill.execute(
                sql,
                dto.getPaddyId(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getMilledQuantity(),
                dto.getBrokenRice(),
                dto.getHusk(),
                dto.getBran(),
                dto.getMillingId()
        );
    }

    public static boolean deleteMillingProcess(MillingProcessdto dto) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM milling_process WHERE milling_id=?";
        return CrudUtill.execute(sql, dto.getMillingId());
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT milling_id FROM milling_process ORDER BY milling_id DESC LIMIT 1";
        ResultSet rs = CrudUtill.execute(sql);

        if (rs.next()) {
            String lastId = rs.getString(1);
            int lastNumber = Integer.parseInt(lastId.substring(1));
            return String.format("M%03d", lastNumber + 1);
        }
        return "M001";
    }

    public static ArrayList<String> getAllPaddyIds() throws SQLException, ClassNotFoundException {
        String sql = "SELECT paddy_id FROM milling_process";
        ResultSet rs = CrudUtill.execute(sql);

        ArrayList<String> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getString("paddy_id"));
        }
        return ids;
    }

    public static boolean isPaddyIdExists(String paddyId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT paddy_id FROM milling_process WHERE paddy_id = ?";
        ResultSet rs = CrudUtill.execute(sql, paddyId);
        return rs.next();
    }
}