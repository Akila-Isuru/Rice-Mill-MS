package lk.ijse.gdse74.mytest2.responsive.model;

import lk.ijse.gdse74.mytest2.responsive.dto.AttendanceDto;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AttendanceModel {

    public String getNextAttendanceId() throws SQLException {
        ResultSet resultSet = CrudUtill.execute("SELECT attendance_id FROM attendance ORDER BY attendance_id DESC LIMIT 1");
        char tableChar = 'A'; // For Attendance ID
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

    public boolean saveAttendance(AttendanceDto attendanceDto) throws SQLException {
        return CrudUtill.execute(
                "INSERT INTO attendance (attendance_id, employee_id, date, status, in_time, out_time, hours_worked) VALUES (?,?,?,?,?,?,?)",
                attendanceDto.getAttendanceId(),
                attendanceDto.getEmployeeId(),
                Date.valueOf(attendanceDto.getDate()),
                attendanceDto.getStatus(),
                (attendanceDto.getInTime() != null) ? Time.valueOf(attendanceDto.getInTime()) : null,
                (attendanceDto.getOutTime() != null) ? Time.valueOf(attendanceDto.getOutTime()) : null,
                attendanceDto.getHoursWorked()
        );
    }

    public boolean updateAttendance(AttendanceDto attendanceDto) throws SQLException {
        return CrudUtill.execute(
                "UPDATE attendance SET employee_id = ?, date = ?, status = ?, in_time = ?, out_time = ?, hours_worked = ? WHERE attendance_id = ?",
                attendanceDto.getEmployeeId(),
                Date.valueOf(attendanceDto.getDate()),
                attendanceDto.getStatus(),
                (attendanceDto.getInTime() != null) ? Time.valueOf(attendanceDto.getInTime()) : null,
                (attendanceDto.getOutTime() != null) ? Time.valueOf(attendanceDto.getOutTime()) : null,
                attendanceDto.getHoursWorked(),
                attendanceDto.getAttendanceId()
        );
    }

    public boolean deleteAttendance(String attendanceId) throws SQLException {
        String sql = "DELETE FROM attendance WHERE attendance_id=?";
        return CrudUtill.execute(sql, attendanceId);
    }

    public ArrayList<AttendanceDto> getAllAttendance() throws SQLException {
        ResultSet rs = CrudUtill.execute("SELECT * FROM attendance");
        ArrayList<AttendanceDto> attendanceList = new ArrayList<>();
        while (rs.next()) {
            attendanceList.add(new AttendanceDto(
                    rs.getString("attendance_id"),
                    rs.getString("employee_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("status"),
                    (rs.getTime("in_time") != null) ? rs.getTime("in_time").toLocalTime() : null,
                    (rs.getTime("out_time") != null) ? rs.getTime("out_time").toLocalTime() : null,
                    rs.getDouble("hours_worked")
            ));
        }
        return attendanceList;
    }

    public ArrayList<AttendanceDto> getAttendanceByEmployeeIdAndMonth(String employeeId, LocalDate monthStart, LocalDate monthEnd) throws SQLException {
        ResultSet rs = CrudUtill.execute(
                "SELECT * FROM attendance WHERE employee_id = ? AND date BETWEEN ? AND ?",
                employeeId, Date.valueOf(monthStart), Date.valueOf(monthEnd)
        );
        ArrayList<AttendanceDto> attendanceList = new ArrayList<>();
        while (rs.next()) {
            attendanceList.add(new AttendanceDto(
                    rs.getString("attendance_id"),
                    rs.getString("employee_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("status"),
                    (rs.getTime("in_time") != null) ? rs.getTime("in_time").toLocalTime() : null,
                    (rs.getTime("out_time") != null) ? rs.getTime("out_time").toLocalTime() : null,
                    rs.getDouble("hours_worked")
            ));
        }
        return attendanceList;
    }

    public boolean isAttendanceRecordedForDate(String employeeId, LocalDate date) throws SQLException {
        ResultSet rs = CrudUtill.execute(
                "SELECT COUNT(*) FROM attendance WHERE employee_id = ? AND date = ?",
                employeeId, Date.valueOf(date)
        );
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}