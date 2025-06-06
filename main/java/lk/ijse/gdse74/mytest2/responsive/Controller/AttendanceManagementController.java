package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse74.mytest2.responsive.dto.AttendanceDto;
import lk.ijse.gdse74.mytest2.responsive.dto.Employeedto;
import lk.ijse.gdse74.mytest2.responsive.model.AttendanceModel;
import lk.ijse.gdse74.mytest2.responsive.model.EmployeeModel;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AttendanceManagementController implements Initializable {

    @FXML private AnchorPane attendanceManagementAnc;
    @FXML private ComboBox<String> cmbEmployeeId;
    @FXML private DatePicker dpDate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TextField txtInTime;
    @FXML private TextField txtOutTime;
    @FXML private TableView<AttendanceDto> tblAttendance;
    @FXML private TableColumn<AttendanceDto, String> colAttendanceId;
    @FXML private TableColumn<AttendanceDto, String> colAttEmployeeId;
    @FXML private TableColumn<AttendanceDto, LocalDate> colDate;
    @FXML private TableColumn<AttendanceDto, String> colStatus;
    @FXML private TableColumn<AttendanceDto, LocalTime> colInTime;
    @FXML private TableColumn<AttendanceDto, LocalTime> colOutTime;
    @FXML private TableColumn<AttendanceDto, Double> colHoursWorked;
    @FXML private Label lblAttendanceCount;
    @FXML private ComboBox<String> cmbSalaryEmployeeId;
    @FXML private DatePicker dpSalaryMonth;
    @FXML private Label lblCalculatedSalary;

    private AttendanceModel attendanceModel = new AttendanceModel();
    private EmployeeModel employeeModel = new EmployeeModel();
    private ObservableList<AttendanceDto> attendanceMasterData = FXCollections.observableArrayList();

    private final String timePattern = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateEmployeeIds();
        populateStatusComboBox();
        setupAttendanceTable();
        loadAttendanceTable();
        setupTableSelectionListener();
        setupSalaryEmployeeIds();


        cmbEmployeeId.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dpDate.getValue() != null) {
                try {
                    AttendanceDto existingAttendance = getAttendanceForEmployeeAndDate(newVal, dpDate.getValue());
                    if (existingAttendance != null) {
                        fillAttendanceFields(existingAttendance);
                    } else {
                        clearAttendanceFieldsWithoutId();
                    }
                } catch (SQLException e) {
                    showAlert("Error checking existing attendance: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });


        dpDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cmbEmployeeId.getValue() != null) {
                try {
                    AttendanceDto existingAttendance = getAttendanceForEmployeeAndDate(cmbEmployeeId.getValue(), newVal);
                    if (existingAttendance != null) {
                        fillAttendanceFields(existingAttendance);
                    } else {
                        clearAttendanceFieldsWithoutId();
                    }
                } catch (SQLException e) {
                    showAlert("Error checking existing attendance: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateEmployeeIds() {
        try {
            ArrayList<Employeedto> employees = employeeModel.viewAllEmployees();
            ObservableList<String> employeeIds = FXCollections.observableArrayList();
            for (Employeedto emp : employees) {
                employeeIds.add(emp.getEmployeeId());
            }
            cmbEmployeeId.setItems(employeeIds);
            cmbEmployeeId.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            showAlert("Failed to load employee IDs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateStatusComboBox() {
        cmbStatus.setItems(FXCollections.observableArrayList("Present", "Absent", "Half-day"));
        cmbStatus.getSelectionModel().selectFirst();
    }

    private void setupAttendanceTable() {
        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colAttEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        colHoursWorked.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
    }

    private void loadAttendanceTable() {
        try {
            ArrayList<AttendanceDto> attendanceList = attendanceModel.getAllAttendance();
            attendanceMasterData.setAll(attendanceList);
            tblAttendance.setItems(attendanceMasterData);
            updateAttendanceCount();
        } catch (SQLException e) {
            showAlert("Failed to load attendance records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateAttendanceCount() {
        lblAttendanceCount.setText("Attendance Records: " + tblAttendance.getItems().size());
    }

    private void setupTableSelectionListener() {
        tblAttendance.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillAttendanceFields(newSelection);
            }
        });
    }

    private void fillAttendanceFields(AttendanceDto attendance) {
        cmbEmployeeId.setDisable(true);
        dpDate.setDisable(true);

        cmbEmployeeId.setValue(attendance.getEmployeeId());
        dpDate.setValue(attendance.getDate());
        cmbStatus.setValue(attendance.getStatus());
        txtInTime.setText(attendance.getInTime() != null ? attendance.getInTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
        txtOutTime.setText(attendance.getOutTime() != null ? attendance.getOutTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
    }

    private void clearAttendanceFieldsWithoutId() {
        cmbEmployeeId.setDisable(false);
        dpDate.setDisable(false);

        cmbStatus.getSelectionModel().clearSelection();
        txtInTime.clear();
        txtOutTime.clear();
    }

    private void setupSalaryEmployeeIds() {
        try {
            ArrayList<Employeedto> employees = employeeModel.viewAllEmployees();
            ObservableList<String> employeeIds = FXCollections.observableArrayList();
            for (Employeedto emp : employees) {
                employeeIds.add(emp.getEmployeeId());
            }
            cmbSalaryEmployeeId.setItems(employeeIds);
            cmbSalaryEmployeeId.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            showAlert("Failed to load employee IDs for salary calculation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private AttendanceDto getAttendanceForEmployeeAndDate(String empId, LocalDate date) throws SQLException {
        ArrayList<AttendanceDto> records = attendanceModel.getAttendanceByEmployeeIdAndMonth(empId, date, date);
        return records.stream()
                .filter(a -> a.getEmployeeId().equals(empId) && a.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    @FXML
    void btnAddAttendanceOnAction(ActionEvent event) {
        String empId = cmbEmployeeId.getValue();
        LocalDate date = dpDate.getValue();
        String status = cmbStatus.getValue();
        String inTimeStr = txtInTime.getText();
        String outTimeStr = txtOutTime.getText();

        if (empId == null || date == null || status == null || status.isEmpty()) {
            showAlert("Please fill in all required fields: Employee ID, Date, Status.");
            return;
        }

        LocalTime inTime = null;
        LocalTime outTime = null;
        Double hoursWorked = 0.0;

        if (status.equals("Present") || status.equals("Half-day")) {
            if (!inTimeStr.matches(timePattern) || !outTimeStr.matches(timePattern)) {
                showAlert("Please enter valid times in HH:MM format for In Time and Out Time.");
                return;
            }
            try {
                inTime = LocalTime.parse(inTimeStr);
                outTime = LocalTime.parse(outTimeStr);

                if (inTime.isAfter(outTime)) {
                    showAlert("In Time cannot be after Out Time.");
                    return;
                }

                Duration duration = Duration.between(inTime, outTime);
                hoursWorked = duration.toMinutes() / 60.0;
                hoursWorked = Math.round(hoursWorked * 100.0) / 100.0;

            } catch (Exception e) {
                showAlert("Invalid time format. Please use HH:MM.");
                return;
            }
        }

        try {
            String attendanceId;
            boolean isUpdate = false;
            AttendanceDto existingAttendance = getAttendanceForEmployeeAndDate(empId, date);

            if (existingAttendance != null) {
                attendanceId = existingAttendance.getAttendanceId();
                isUpdate = true;
            } else {
                attendanceId = attendanceModel.getNextAttendanceId();
            }

            AttendanceDto attendanceDto = new AttendanceDto(attendanceId, empId, date, status, inTime, outTime, hoursWorked);

            boolean success;
            if (isUpdate) {
                success = attendanceModel.updateAttendance(attendanceDto);
            } else {
                success = attendanceModel.saveAttendance(attendanceDto);
            }

            if (success) {
                showAlert("Attendance " + (isUpdate ? "updated" : "added") + " successfully!");
                clearAttendanceFieldsAndReload();
            } else {
                showAlert("Failed to " + (isUpdate ? "update" : "add") + " attendance.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    void btnClearFieldsOnAction(ActionEvent event) {
        clearAttendanceFieldsAndReload();
    }

    private void clearAttendanceFieldsAndReload() {
        clearAttendanceFieldsWithoutId();
        loadAttendanceTable();
    }

    @FXML
    void btnCalculateSalaryOnAction(ActionEvent event) {
        String empId = cmbSalaryEmployeeId.getValue();
        LocalDate selectedDate = dpSalaryMonth.getValue();

        if (empId == null || selectedDate == null) {
            showAlert("Please select an Employee ID and a Month to calculate salary.");
            return;
        }

        try {
            Employeedto employee = employeeModel.viewAllEmployees().stream()
                    .filter(e -> e.getEmployeeId().equals(empId))
                    .findFirst()
                    .orElse(null);

            if (employee == null) {
                showAlert("Employee not found.");
                return;
            }

            BigDecimal basicSalary = employee.getBasicSalary();
            if (basicSalary == null || basicSalary.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert("Employee " + empId + " has no or invalid basic salary defined. Cannot calculate.");
                lblCalculatedSalary.setText("LKR 0.00 (No Basic Salary)");
                return;
            }

            YearMonth yearMonth = YearMonth.from(selectedDate);
            LocalDate monthStart = yearMonth.atDay(1);
            LocalDate monthEnd = yearMonth.atEndOfMonth();

            ArrayList<AttendanceDto> monthlyAttendance = attendanceModel.getAttendanceByEmployeeIdAndMonth(empId, monthStart, monthEnd);

            double totalHoursWorked = 0.0;
            int presentDays = 0;
            int absentDays = 0;
            int halfDays = 0;

            for (AttendanceDto attendance : monthlyAttendance) {
                if ("Present".equals(attendance.getStatus())) {
                    presentDays++;
                    if (attendance.getHoursWorked() != null) {
                        totalHoursWorked += attendance.getHoursWorked();
                    } else {
                        totalHoursWorked += 8.0;
                    }
                } else if ("Half-day".equals(attendance.getStatus())) {
                    halfDays++;
                    if (attendance.getHoursWorked() != null) {
                        totalHoursWorked += attendance.getHoursWorked();
                    } else {
                        totalHoursWorked += 4.0;
                    }
                } else if ("Absent".equals(attendance.getStatus())) {
                    absentDays++;
                }
            }

            double standardHoursPerDay = 8.0;
            int totalDaysInMonth = monthEnd.getDayOfMonth();
            double totalPossibleWorkingHoursInMonth = totalDaysInMonth * standardHoursPerDay;

            BigDecimal calculatedSalary;
            if (totalPossibleWorkingHoursInMonth > 0) {
                BigDecimal hourlyRate = basicSalary.divide(BigDecimal.valueOf(totalPossibleWorkingHoursInMonth), BigDecimal.ROUND_HALF_UP);
                calculatedSalary = hourlyRate.multiply(BigDecimal.valueOf(totalHoursWorked));
            } else {
                calculatedSalary = BigDecimal.ZERO;
            }

            lblCalculatedSalary.setText(String.format("LKR %,.2f", calculatedSalary));

            showAlert("Salary for " + employee.getName() + " (" + empId + ") for " + yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")) + ":\n" +
                    "  Total Hours Worked: " + String.format("%.2f", totalHoursWorked) + " hours\n" +
                    "  Present Days: " + presentDays + "\n" +
                    "  Absent Days: " + absentDays + "\n" +
                    "  Half Days: " + halfDays + "\n" +
                    "  Calculated Salary: LKR " + String.format("%,.2f", calculatedSalary));

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error during salary calculation: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error calculating salary: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}