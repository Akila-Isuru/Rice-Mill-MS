package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.db.DBConnection;
import lk.ijse.gdse74.mytest2.responsive.dto.SalesOrderdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalesOrderModel {
    public static ArrayList<SalesOrderdto> viewAllSalesOrder ()throws ClassNotFoundException, SQLException {
        ResultSet rs = CrudUtill.execute("select * from sales_order");
        ArrayList<SalesOrderdto> salesOrders = new ArrayList<>();
        while (rs.next()) {
            SalesOrderdto salesOrderdto = new SalesOrderdto(
                    rs.getString("order_id"),
                    rs.getString("customer_id"),
                    rs.getDate("order_date"),
                    rs.getInt("total_amount")
            );
            salesOrders.add(salesOrderdto);
        }
        return salesOrders;
    }

    public static boolean placeOrder(SalesOrderdto salesOrderdto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            boolean isSave = CrudUtill.execute(
                    "insert into sales_order values (?,?,?,?)",
                    salesOrderdto.getOrderId(),
                    salesOrderdto.getCustomerId(),
                    salesOrderdto.getOrderDate(),
                    salesOrderdto.getOrderAmount()

            );
            if (isSave) {
                boolean isDetailSave = new SalesOrderDetails().saveOrderDetailsList(salesOrderdto.getCartList());
                if (isDetailSave) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;

        }catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean saveOrder(SalesOrderdto salesOrderdto)throws ClassNotFoundException, SQLException {
        return CrudUtill.execute( "insert into sales_order values(?,?,?,?,?)",
                salesOrderdto.getOrderId(),
                salesOrderdto.getCustomerId(),
                salesOrderdto.getOrderDate(),
                salesOrderdto.getOrderAmount()

                );
    }
    public boolean DeleteSalesOrder(SalesOrderdto salesOrderdto)throws ClassNotFoundException, SQLException {
        String sql = "delete from sales_order where order_id=?";
        return CrudUtill.execute(sql,salesOrderdto.getOrderId());
    }
    public boolean UpdateSalesOrder(SalesOrderdto salesOrderdto)throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
          "update sales_order set customer_id = ?, order_date = ?, total_amount = ?, payment_status = ? where order_id =? ",
          salesOrderdto.getCustomerId(),
          salesOrderdto.getOrderDate(),
          salesOrderdto.getOrderAmount(),
          salesOrderdto.getOrderId()
        );
    }
    public String getNextId() throws SQLException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        String sql = "select customer_id from customer order by customer_id desc limit 1";
//        PreparedStatement pst = connection.prepareStatement(sql);

//        ResultSet resultSet = pst.executeQuery();
        ResultSet resultSet = CrudUtill.execute("select order_id from sales_order order by order_id desc limit 1");
        char tableChar = 'O'; // Use any character Ex:- customer table for C, item table for I
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
