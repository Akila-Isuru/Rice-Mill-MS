package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Usersdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersModel {
    public ArrayList<Usersdto> viewAllUsers() throws SQLException,ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM users");
        ArrayList<Usersdto> users = new ArrayList<>();
        while (rs.next()) {
            Usersdto user = new Usersdto(
                    rs.getString("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("contact_number")
            );
            users.add(user);
        }
        return users;
    }
    public boolean saveUser(Usersdto userdto) throws SQLException,ClassNotFoundException {
        return CrudUtill.execute(
                "insert into users values (?,?,?,?,?)",
                userdto.getUser_id(),
                userdto.getName(),
                userdto.getEmail(),
                userdto.getRole(),
                userdto.getContact_number()

        );
    }
    public boolean deleteUser(Usersdto userdto) throws SQLException,ClassNotFoundException {
        String sql = "delete from users where user_id=?";
        return CrudUtill.execute(sql, userdto.getUser_id());
    }
    public boolean updateUser(Usersdto userdto) throws SQLException,ClassNotFoundException {
        return CrudUtill.execute(
                "update users set name =?,email =?,role=?,contact_number=? where user_id = ?",
                userdto.getName(),
                userdto.getEmail(),
                userdto.getRole(),
                userdto.getContact_number(),
                userdto.getUser_id()
        );
    }

    public String getNextId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("select user_id from users order by user_id desc limit 1");
        char tableChar = 'U'; // Use any character Ex:- customer table for C, item table for I
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


