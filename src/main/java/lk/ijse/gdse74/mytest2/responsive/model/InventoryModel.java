package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Inventorydto;
import lk.ijse.gdse74.mytest2.responsive.dto.SalesOrderDetailsdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryModel {
    public static ArrayList<Inventorydto> viewAllInventory() throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM inventory");
        ArrayList<Inventorydto> inventory = new ArrayList<>();
        while (rs.next()) {
            Inventorydto inventorydto = new Inventorydto(
                    rs.getString("inventory_id"),
                    rs.getString("product_id"),
                    rs.getInt("curent_stock_bags"),
                    rs.getDate("last_updated")
            );
            inventory.add(inventorydto);
        }
        return inventory;
    }

    public static ArrayList<String> getAllproductIds() throws SQLException {
        ResultSet rst = CrudUtill.execute(
                "select product_id from inventory ");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String productId = rst.getString(1);
            list.add(productId);

        }
        return list;

    }

    public boolean saveInventory(Inventorydto inventorydto) throws SQLException {
        return CrudUtill.execute(
                "insert into inventory values (?,?,?,?)",
                inventorydto.getId(),
                inventorydto.getProductId(),
                inventorydto.getCurrentStockBags(),
                inventorydto.getLastUpdated()

        );
    }
    public boolean deleteInventory(Inventorydto inventorydto) throws SQLException {
           String sql = "delete from inventory where inventory_id = ?";
           return CrudUtill.execute(sql, inventorydto.getId());


    }
    public boolean updateInventory(Inventorydto inventorydto) throws SQLException {
        return CrudUtill.execute(
                "update inventory set product_id =?,curent_stock_bags =?, last_updated =? where inventory_id =?",
                   inventorydto.getProductId(),
                   inventorydto.getCurrentStockBags(),
                   inventorydto.getLastUpdated(),
                inventorydto.getId()
        );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select inventory_id from inventory order by inventory_id desc limit 1");
        char tableChar = 'I'; // Use any character Ex:- customer table for C, item table for I
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
