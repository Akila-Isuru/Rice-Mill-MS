package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.QualityCheckdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QualityCheckModel {
    public static ArrayList<QualityCheckdto> viewAllQualityCheck() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM quality_check");
        ArrayList<QualityCheckdto> checklist = new ArrayList<>();
        while (rs.next()) {
            QualityCheckdto qualityCheckdto = new QualityCheckdto(
                    rs.getString("check_id"),
                    rs.getString("paddy_id"),
                    rs.getString("moisture_level"),
                    rs.getString("foreign_material"),
                    rs.getString("broken_precentage"),
                    rs.getDate("inspection_date")
            );
            checklist.add(qualityCheckdto);
        }
        return checklist;
    }

    public boolean saveQualityCheck(QualityCheckdto qualityCheckdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "INSERT INTO quality_check VALUES (?,?,?,?,?,?)",
                qualityCheckdto.getCheckId(),
                qualityCheckdto.getPaddyId(),
                qualityCheckdto.getMoistureLevel(),
                qualityCheckdto.getForeignMaterial(),
                qualityCheckdto.getBrokenPrecentage(),
                qualityCheckdto.getInceptionDate()
        );
    }

    public boolean deleteQualityCheck(QualityCheckdto qualityCheckdto) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM quality_check WHERE check_id=?";
        return CrudUtill.execute(sql, qualityCheckdto.getCheckId());
    }

    public boolean updateQualityCheck(QualityCheckdto qualityCheckdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "UPDATE quality_check SET paddy_id=?, moisture_level=?, foreign_material=?, broken_precentage=?, inspection_date=? WHERE check_id=?",
                qualityCheckdto.getPaddyId(),
                qualityCheckdto.getMoistureLevel(),
                qualityCheckdto.getForeignMaterial(),
                qualityCheckdto.getBrokenPrecentage(),
                qualityCheckdto.getInceptionDate(),
                qualityCheckdto.getCheckId()
        );
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtill.execute("SELECT check_id FROM quality_check ORDER BY check_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            try {
                int lastIdNumber = Integer.parseInt(lastId.substring(2)); // Skip 'QC' and parse to int
                return String.format("QC%03d", lastIdNumber + 1); // Increment and format
            } catch (NumberFormatException e) {
                // If parsing fails, return QC001
                return "QC001";
            }
        }
        return "QC001"; // If no records found, start with QC001
    }
}