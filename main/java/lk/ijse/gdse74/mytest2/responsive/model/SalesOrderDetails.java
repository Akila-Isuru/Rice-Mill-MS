package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.SalesOrderDetailsdto;
import lk.ijse.gdse74.mytest2.responsive.dto.SalesOrderdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalesOrderDetails {
    private final InventoryModel inventoryModel = new InventoryModel();
    public String getNextId() throws SQLException {




        ResultSet resultSet = CrudUtill.execute("select order_id from sales_order order by order_id desc limit 1");
        String tableChar = "OD";
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(2);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public boolean saveOrderDetailsList(ArrayList<SalesOrderDetailsdto> cartList) throws SQLException {
        for (SalesOrderDetailsdto salesOrderDetailsdto : cartList) {
            boolean isDetailsSaved = saveOrderDetails(salesOrderDetailsdto);
            if (!isDetailsSaved) {
                return false;

            }

            boolean isUpdated = FinishedProductModel.reduceQty(salesOrderDetailsdto);
            if (!isUpdated) {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrderDetails(SalesOrderDetailsdto dto) throws SQLException {
return CrudUtill.execute("insert into sales_order_details values (?,?,?,?,?)",
        dto.getOrderId(),
        dto.getProductId(),
        dto.getUnitPrice(),
        dto.getQty(),
        dto.getTotalPrice()
);
    }
}
