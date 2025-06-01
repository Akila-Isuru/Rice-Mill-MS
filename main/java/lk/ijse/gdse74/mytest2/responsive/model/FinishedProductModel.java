package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.FinishedProductdto;
import lk.ijse.gdse74.mytest2.responsive.dto.SalesOrderDetailsdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinishedProductModel {

    public static ArrayList<FinishedProductdto> viewAllFinishedProduct() throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from finished_product");
        ArrayList<FinishedProductdto> finishedProducts = new ArrayList<>();
        while (rs.next()) {
            FinishedProductdto fp = new FinishedProductdto(
                    rs.getString("product_id"),
                    rs.getString("milling_id"),
                    rs.getString("product_type"),
                    rs.getDouble("packaging_size_kg"),
                    rs.getInt("total_quantity_bags"),
                    rs.getInt("price_per_bag")
            );
            finishedProducts.add(fp);
        }
        return finishedProducts;
    }

    public static FinishedProductdto findById(String selectItemDetails) throws SQLException {
        ResultSet rst = CrudUtill.execute("select * from finished_product where product_id=?",
                selectItemDetails);
        if (rst.next()) {
            return new FinishedProductdto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5),
                    rst.getInt(6));
        }
        return null;
    }

    public static ArrayList<String> getAllItemIds() throws SQLException {
        ResultSet rst = CrudUtill.execute("select product_id from finished_product");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public static boolean reduceQty(SalesOrderDetailsdto salesOrderDetailsdto) throws SQLException {
        return CrudUtill.execute(
                "update finished_product set total_quantity_bags = total_quantity_bags - ? where product_id = ?",
                salesOrderDetailsdto.getUnitPrice(),
                salesOrderDetailsdto.getProductId()
        );
    }

    public boolean saveFinishedProduct(FinishedProductdto finishedProductdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "insert into finished_product values (?,?,?,?,?,?)",
                finishedProductdto.getProductId(),
                finishedProductdto.getMillingId(),
                finishedProductdto.getProductType(),
                finishedProductdto.getPackageSize(),
                finishedProductdto.getQuantityBags(),
                finishedProductdto.getPricePerBag()
        );
    }

    public boolean deleteFinishedProduct(FinishedProductdto finishedProductdto) throws ClassNotFoundException, SQLException {
        String sql = "delete from finished_product where product_id=?";
        return CrudUtill.execute(sql, finishedProductdto.getProductId());
    }

    public boolean updateFinishedProduct(FinishedProductdto finishedProductdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "update finished_product set milling_id=?, product_type=?, packaging_size_kg=?, total_quantity_bags=?, price_per_bag=? where product_id=?",
                finishedProductdto.getMillingId(),
                finishedProductdto.getProductType(),
                finishedProductdto.getPackageSize(),
                finishedProductdto.getQuantityBags(),
                finishedProductdto.getPricePerBag(),
                finishedProductdto.getProductId()
        );
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("SELECT product_id FROM finished_product ORDER BY product_id DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString("product_id");
            int nextNum = Integer.parseInt(lastId.substring(2)) + 1; // Extract number after "FP"
            return String.format("FP%03d", nextNum);
        }
        return "FP001"; // Return first ID if no records exist
    }
}