package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Employeedto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

    public ArrayList<Employeedto> viewAllEmployees() throws SQLException {
        ResultSet rs = CrudUtill.execute("select * from employees");
        ArrayList<Employeedto> employees = new ArrayList<>();
        while (rs.next()) {
            Employeedto employeedto = new Employeedto(
                    rs.getString("employee_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("contact_number"),
                    rs.getString("job_role"),
                    rs.getBigDecimal("basic_salary")
            );
            employees.add(employeedto);
        }
        return employees;
    }

    public boolean saveEmployee(Employeedto employeedto) throws SQLException {
        return CrudUtill.execute(
                "insert into employees values (?,?,?,?,?,?)",
                employeedto.getEmployeeId(),
                employeedto.getName(),
                employeedto.getAddress(),
                employeedto.getContactNumber(),
                employeedto.getJobRole(),
                employeedto.getBasicSalary()
        );
    }

    public boolean deleteEmployee(Employeedto employeedto) throws SQLException {
        String sql = "delete from employees where employee_id=?";
        return CrudUtill.execute(sql, employeedto.getEmployeeId());
    }

    public boolean updateEmployee(Employeedto employeedto) throws SQLException {
        return CrudUtill.execute(
                "update employees set name = ?, address = ?, contact_number = ?, job_role = ?, basic_salary = ? where employee_id = ?",
                employeedto.getName(),
                employeedto.getAddress(),
                employeedto.getContactNumber(),
                employeedto.getJobRole(),
                employeedto.getBasicSalary(),
                employeedto.getEmployeeId()
        );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select employee_id from employees order by employee_id desc limit 1");
        char tableChar = 'E'; // For Employee ID
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public static int getEmployeeCount() throws SQLException {
        ResultSet rs = CrudUtill.execute("SELECT COUNT(*) FROM employees");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}