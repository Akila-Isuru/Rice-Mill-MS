package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Suppliersdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliersModel {
    public static ArrayList<Suppliersdto> viewAllSuppliers() throws SQLException {
        ResultSet rs = CrudUtill.execute("select * from suppliers");
        ArrayList<Suppliersdto> suppliers = new ArrayList<>();
        while (rs.next()) {
            Suppliersdto supplierdto = new Suppliersdto(
                    rs.getString("supplier_id"),
                    rs.getString("name"),
                    rs.getString("cotact_number"),
                    rs.getString("address"),
                    rs.getString("email")
            );
            suppliers.add(supplierdto);
        }
        return suppliers;
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select supplier_id from suppliers order by supplier_id desc limit 1");
        char tableChar = 'S';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

    public static boolean deleteSupplier(Suppliersdto suppliersdto) throws SQLException {
        String sql = "delete from suppliers where supplier_id=?";
        return CrudUtill.execute(sql,suppliersdto.getSupplierId());
    }

    public boolean saveSupplier(Suppliersdto supplierdto) throws SQLException,ClassNotFoundException {
        return CrudUtill.execute(
                "insert into suppliers values (?,?,?,?,?)",
                supplierdto.getSupplierId(),
                supplierdto.getName(),
                supplierdto.getContactNumber(),
                supplierdto.getAddress(),
                supplierdto.getEmail()
        );
    }

    public boolean updateSupplier(Suppliersdto suppliersdto) throws SQLException {
        return CrudUtill.execute(
                "update suppliers set name=?, cotact_number=?, address=?, email=? where supplier_id=?",
                suppliersdto.getName(),
                suppliersdto.getContactNumber(),
                suppliersdto.getAddress(),
                suppliersdto.getEmail(),
                suppliersdto.getSupplierId()
        );
    }

    public static int getSupplierCount() throws SQLException {
        ResultSet rs = CrudUtill.execute("SELECT COUNT(*) FROM suppliers");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}