package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.RawPaddydto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RawPaddyModel {
    public static ArrayList<RawPaddydto> viewAllPaddy() throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from raw_paddy");
        ArrayList<RawPaddydto> paddys = new ArrayList<>();
        while (rs.next()) {
            RawPaddydto paddy = new RawPaddydto(
                    rs.getString("paddy_id"),
                    rs.getString("supplier_id"),
                    rs.getString("farmer_id"),
                    rs.getDouble("quantity_kg"),
                    rs.getDouble("moisture_level"),
                    rs.getDouble("purchase_price_per_kg"),
                    rs.getDate("purchase_date")
            );
            paddys.add(paddy);
        }
        return paddys;
    }
    public boolean SaveRawPaddy(RawPaddydto rawPaddydto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "insert into raw_paddy values (?,?,?,?,?,?,?)",
                rawPaddydto.getPaddyId(),
                rawPaddydto.getSupplierId(),
                rawPaddydto.getFarmerId(),
                rawPaddydto.getQuantity(),
                rawPaddydto.getMoisture(),
                rawPaddydto.getPurchasePrice(),
                rawPaddydto.getPurchaseDate()

        );
    }
    public boolean deleteRawPaddy(RawPaddydto rawPaddydto) throws ClassNotFoundException, SQLException {
        String sql = "delete from raw_paddy where paddy_id=?";
        return CrudUtill.execute(sql, rawPaddydto.getPaddyId());
    }
    public boolean updateRawPaddy(RawPaddydto rawPaddydto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
               "update raw_paddy set supplier_id=?,farmer_id=?,quantity_kg=?,moisture_level=?,purchase_price_per_kg=?,purchase_date=? where paddy_id =?",
               rawPaddydto.getSupplierId(),
               rawPaddydto.getFarmerId(),
               rawPaddydto.getQuantity(),
               rawPaddydto.getMoisture(),
               rawPaddydto.getPurchasePrice(),
               rawPaddydto.getPurchaseDate(),
               rawPaddydto.getPaddyId()
        );
    }
}

