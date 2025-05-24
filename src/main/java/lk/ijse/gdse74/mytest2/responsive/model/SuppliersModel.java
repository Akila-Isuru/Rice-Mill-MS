package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.dto.Suppliersdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;

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
                "update suppliers set name = ?, cotact_number = ?,address = ? ,email =? where supplier_id = ?",
                suppliersdto.getSupplierId(),
                suppliersdto.getName(),
                suppliersdto.getContactNumber(),
                suppliersdto.getAddress(),
                suppliersdto.getEmail()



        );

    }


}

