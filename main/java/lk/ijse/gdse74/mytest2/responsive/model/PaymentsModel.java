package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Paymentsdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentsModel{
    public static ArrayList<Paymentsdto> viewPayments() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("select * from payments");
        ArrayList<Paymentsdto> payments = new ArrayList<>();
        while (rs.next()) {
           Paymentsdto payment = new Paymentsdto(
                   rs.getString("payment_id"),
                   rs.getString("order_id"),
                   rs.getDate("payment_date"),
                   rs.getString("payment_method"),
                   rs.getInt("amount_paid")
           );
           payments.add(payment);

        }
        return payments;

    }

    public static ArrayList<String> getAlCustomerIds() throws SQLException {
        ResultSet rst = CrudUtill.execute("select order_id from sales_order");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public boolean savePayment(Paymentsdto paymentdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "insert into payments values (?,?,?,?,?)",
                paymentdto.getPaymentId(),
                paymentdto.getOrderId(),
                paymentdto.getPaymentDate(),
                paymentdto.getPaymentMethods(),
                paymentdto.getAmountPaid()

        );
    }
    public boolean deletePayment(Paymentsdto paymentdto) throws SQLException, ClassNotFoundException {
        String sql = "delete from payments where payment_id=?";
        return CrudUtill.execute(sql, paymentdto.getPaymentId());
    }
    public boolean updatePayment(Paymentsdto paymentdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
          "update payments set order_id =?,payment_date= ?,payment_method =?,amount_paid =? where payment_id =?",
          paymentdto.getOrderId(),
          paymentdto.getPaymentDate(),
          paymentdto.getPaymentMethods(),
                paymentdto.getAmountPaid(),
                paymentdto.getPaymentId()
        );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select payment_id from payments order by payment_id desc limit 1");
        String prefix = "PAY";
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(prefix.length());
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(prefix + "%03d", nextIdNumber); // "PAY002"
            return nextIdString;
        }
        return prefix + "001";
    }
}
