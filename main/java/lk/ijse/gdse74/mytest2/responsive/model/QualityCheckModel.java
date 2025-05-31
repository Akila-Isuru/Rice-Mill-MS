package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.QualityCheckdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QualityCheckModel {
    public static ArrayList<QualityCheckdto> viewAllQualityCheck() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("select * from quality_check");
        ArrayList<QualityCheckdto> Checklist = new ArrayList<>();
        while (rs.next()) {
            QualityCheckdto qualityCheckdto = new QualityCheckdto(
                    rs.getString("check_id"),
                    rs.getString("paddy_id"),
                    rs.getString("moisture_level"),
                    rs.getString("foreign_material"),
                    rs.getString("broken_precentage"),
                    rs.getDate("inspection_date")

            );
            Checklist.add(qualityCheckdto);
        }
        return Checklist;
    }
    public  boolean saveQualityCheck(QualityCheckdto qualityCheckdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "insert into quality_check values (?,?,?,?,?,?)",
                qualityCheckdto.getCheckId(),
                qualityCheckdto.getPaddyId(),
                qualityCheckdto.getMoistureLevel(),
                qualityCheckdto.getForeignMaterial(),
                qualityCheckdto.getBrokenPrecentage(),
                qualityCheckdto.getInceptionDate()
        );
    }
    public boolean deleteQualityCheck(QualityCheckdto qualityCheckdto) throws SQLException, ClassNotFoundException {
        String sql = "delete from quality_check where check_id=?";
        return CrudUtill.execute(sql, qualityCheckdto.getCheckId());
    }
    public boolean updateQualityCheck(QualityCheckdto qualityCheckdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "update quality_check set paddy_id =?,moisture_level =?,foreign_material =?,broken_precentage =?,inspection_date =? where check_id=?",
                  qualityCheckdto.getPaddyId(),
                qualityCheckdto.getMoistureLevel(),
                qualityCheckdto.getForeignMaterial(),
                qualityCheckdto.getBrokenPrecentage(),
                qualityCheckdto.getInceptionDate(),
                qualityCheckdto.getCheckId()

        );
    }


}
