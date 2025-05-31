package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Reportsdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportsModel {
    public static ArrayList<Reportsdto> viewAllReports () throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from reports");
        ArrayList<Reportsdto> reports = new ArrayList<>();
        while (rs.next()) {
            Reportsdto reportsdto = new Reportsdto(
                    rs.getString("report_id"),
                    rs.getString("report_type"),
                    rs.getString("generated_date")
            );
            reports.add(reportsdto);
        }
        return reports;
    }
    public boolean saveReport(Reportsdto reportsdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "insert into reports values (?,?,?)",
                reportsdto.getReportId()
                , reportsdto.getReportType()
                , reportsdto.getReportDate()
        );
    }
    public boolean deleteReport(Reportsdto reportsdto) throws ClassNotFoundException, SQLException {
        String sql = "delete from reports where report_id=?";
        return CrudUtill.execute(sql, reportsdto.getReportId());
    }
    public boolean updateReport(Reportsdto reportsdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "update reports set report_type =?,generated_date = ? where report_id =?",
                reportsdto.getReportType(),
                reportsdto.getReportDate(),
                reportsdto.getReportId()

        );
    }
}
