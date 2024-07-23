package View;

import Common.Config;
import Controller.AppTableController;
import Model.Employee;
import Model.EmployeeBaseSales;
import Model.EmployeeHours;
import Model.State;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;

public class Employees extends AppTableView<Employee> {

  public Employees() {
    super(State.getEmployees(), new AppTableController<>());
  }

  @Override
  public void load(Stage stage) {
    try {
      this.stage = stage;
      formView = new EmployeeForm();
      Parent root = buildView();
      controller.setDataTable(dataTable);
      controller.setFormView(formView);
      Scene scene = new Scene(root,1300,600);
      stage.setTitle("Optimize Business - Employees");
      stage.setScene(scene);
      stage.getScene().getStylesheets().add(getClass().getResource("stylesheets/table.css").toExternalForm());
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  List<TableColumn> getTableColumns() {
    TableColumn<Employee, String> nameColumn = new TableColumn<>("Full name");
    TableColumn<Employee, Number> idColumn = new TableColumn<>("ID");
    TableColumn<Employee, String> roleColumn = new TableColumn<>("Role");
    TableColumn<Employee, String> departmentColumn = new TableColumn<>("Department");
    TableColumn<Employee, String> salaryTypeColumn = new TableColumn<>("Salary type");
    TableColumn<Employee, Number> monthlyHoursColumn = new TableColumn<>("Monthly hours");
    TableColumn<Employee, Number> monthlySalesColumn = new TableColumn<>("Monthly sales");
    TableColumn<Employee, String> workPreferenceColumn = new TableColumn<>("Work Preference");
    TableColumn<Employee, Number> hourChangeColumn = new TableColumn<>("Hour change");
    TableColumn<Employee, Number> profitHours = new TableColumn<>("Profit (Hours)");
    TableColumn<Employee, Number> profitNis = new TableColumn<>("Profit (NIS)");

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().getName()));
    departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getName()));
    salaryTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSalaryType().getText()));
    monthlyHoursColumn.setCellValueFactory((cellData) -> {
      int hours = cellData.getValue() instanceof EmployeeHours ?
          ((EmployeeHours)cellData.getValue()).getMonthlyHours() : Config.EMPLOYEE_HOURS_PER_MONTH;
      return new SimpleIntegerProperty(hours);
    });
    monthlySalesColumn.setCellValueFactory((cellData) -> {
      int sales = cellData.getValue() instanceof EmployeeBaseSales ?
          ((EmployeeBaseSales)cellData.getValue()).getMonthlySales() : 0;
      return new SimpleIntegerProperty(sales);
    });
    workPreferenceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWorkPreference().getPreferenceType().getText()));
    hourChangeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getWorkPreference().getHourChange()));
    profitHours.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calculateCompanyProfitInHours()));
    profitNis.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calculateCompanyProfitInNis()));

    return Arrays.asList(nameColumn, idColumn, roleColumn, departmentColumn, salaryTypeColumn, monthlyHoursColumn,
        monthlySalesColumn, workPreferenceColumn, hourChangeColumn, profitHours, profitNis);
  }
}
