package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Customersdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public static String findNameById(String selectedCustomerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtill.execute("select name from customers where customer_id=?",
                selectedCustomerId);
        if(rst.next()){
            return rst.getString("name");
        } else {
            return null;
        }
    }

    public static ArrayList<String> getAllCustomerIds() throws SQLException {
        ResultSet rst = CrudUtill.execute("select customer_id from customers");
        ArrayList<String> list = new ArrayList<>();
        while(rst.next()){
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public ArrayList<Customersdto> getAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtill.execute("select * from customers");
        ArrayList<Customersdto> customers = new ArrayList<>();

        while (rst.next()) {
            Customersdto customer = new Customersdto(
                    rst.getString("customer_id"),
                    rst.getString("name"),
                    rst.getString("contatct_number"),
                    rst.getString("address"),
                    rst.getString("email")
            );
            customers.add(customer);
        }
        return customers;
    }

    public boolean saveCustomer(Customersdto customersdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "insert into customers values (?,?,?,?,?)",
                customersdto.getCustomerId(),
                customersdto.getName(),
                customersdto.getContactNumber(),
                customersdto.getAddress(),
                customersdto.getEmail()
        );
    }

    public boolean deleteCustomer(Customersdto customersdto) throws SQLException {
        String sql = "delete from customers where customer_id=?";
        return CrudUtill.execute(sql, customersdto.getCustomerId());
    }

    public boolean updateCustomer(Customersdto customersdto) throws ClassNotFoundException, SQLException {
        return CrudUtill.execute(
                "update customers set name = ?, contatct_number = ?, address = ?, email = ? where customer_id=?",
                customersdto.getName(),
                customersdto.getContactNumber(),
                customersdto.getAddress(),
                customersdto.getEmail(),
                customersdto.getCustomerId()
        );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select customer_id from customers order by customer_id desc limit 1");
        char tableChar = 'C';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public static int getCustomerCount() throws SQLException {
        ResultSet rs = CrudUtill.execute("SELECT COUNT(*) FROM customers");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}