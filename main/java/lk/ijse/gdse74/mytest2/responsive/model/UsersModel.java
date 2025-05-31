package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.Usersdto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill; // Make sure this path is correct

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersModel {

    /**
     * Retrieves a user from the database based on the provided username and password.
     *
     * @param username The username to search for.
     * @param password The password to match.
     * @return A Usersdto object if a matching user is found, null otherwise.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the JDBC driver class is not found.
     */
    public Usersdto getUserByUsernameAndPassword(String username, String password) throws SQLException, ClassNotFoundException {
        // Assuming 'name' is the column for username in your 'users' table
        ResultSet rs = CrudUtill.execute("SELECT * FROM users WHERE name = ? AND password = ?", username, password);

        if (rs.next()) {
            return new Usersdto(
                    rs.getString("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("contact_number")
            );
        }
        return null; // No matching user found
    }

    public ArrayList<Usersdto> viewAllUsers() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM users");
        ArrayList<Usersdto> users = new ArrayList<>();
        while (rs.next()) {
            Usersdto user = new Usersdto(
                    rs.getString("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("contact_number")
            );
            users.add(user);
        }
        return users;
    }

    public boolean saveUser(Usersdto userdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "INSERT INTO users VALUES (?,?,?,?,?,?)",
                userdto.getUser_id(),
                userdto.getName(),
                userdto.getEmail(),
                userdto.getPassword(),
                userdto.getRole(),
                userdto.getContact_number()
        );
    }

    public boolean deleteUser(Usersdto userdto) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM users WHERE user_id=?";
        return CrudUtill.execute(sql, userdto.getUser_id());
    }

    public boolean updateUser(Usersdto userdto) throws SQLException, ClassNotFoundException {
        return CrudUtill.execute(
                "UPDATE users SET name=?, email=?, password=?, role=?, contact_number=? WHERE user_id=?",
                userdto.getName(),
                userdto.getEmail(),
                userdto.getPassword(),
                userdto.getRole(),
                userdto.getContact_number(),
                userdto.getUser_id()
        );
    }

    public String getNextId() throws SQLException, ClassNotFoundException { // Added ClassNotFoundException
        ResultSet resultSet = CrudUtill.execute("SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1");
        char tableChar = 'U';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}