package View;

import Controller.DepartmentFormController;
import Controller.EmployeeFormController;
import Model.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmployeeForm extends AppFormView<Employee> {

	public EmployeeForm() {
		super(new EmployeeFormController());
	}

	@Override
	public void load(Stage stage) {
		try {
			this.stage = stage;
			Parent root = buildView();
			tableView = new Employees();
			controller.setTableView(tableView);
			controller.setForm(DepartmentFormController.FORM_TYPE.ADD);
			setTableView(tableView);
			Scene scene = new Scene(root, 600, 800);
			stage.setTitle("Optimize Business - Add Employee");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(Stage stage, Employee employee) {
		try {
			this.stage = stage;
			Parent root = buildView();
			tableView = new Employees();
			controller.setTableView(tableView);
			controller.setForm(EmployeeFormController.FORM_TYPE.EDIT, employee);
			setTableView(tableView);
			Scene scene = new Scene(root, 600, 800);
			stage.setTitle("Optimize Business - Edit Employee");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	GridPane setGrid() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20);
		grid.setHgap(30);
		grid.setPadding(new Insets(25));

		Label nameLabel = new Label("Full name");
		TextField nameField = new TextField();
		nameField.setPromptText("Employee name");
		grid.addRow(0, nameLabel, nameField);

		Label idLabel = new Label("ID");
		TextField idField = new TextField();
		idField.setPromptText("Employee ID number");
		grid.addRow(1, idLabel, idField);

		Label departmentLabel = new Label("Department");
		ChoiceBox<Department> departmentChoice = new AppChoiceBox<>(State.getDepartments());
		grid.addRow(2, departmentLabel, departmentChoice);

		Label roleLabel = new Label("Role");
		ChoiceBox<Role> roleChoice = new AppChoiceBox<>(State.getRoles());
		grid.addRow(3, roleLabel, roleChoice);

		Label salaryTypeLabel = new Label("Salary type");
		ChoiceBox<Employee.SalaryType> salaryChoice = new AppChoiceBox<>(Employee.SalaryType.values());
		salaryChoice.setOnAction(event -> ((EmployeeFormController) controller).onSalaryChange());
		grid.addRow(4, salaryTypeLabel, salaryChoice);

		Label monthlyHoursLabel = new Label("Monthly hours");
		TextField monthlyHoursField = new TextField();
		monthlyHoursField.setPromptText("Monthly hours");
		grid.addRow(5, monthlyHoursLabel, monthlyHoursField);

		Label monthlySalesLabel = new Label("Monthly sales");
		TextField monthlySalesField = new TextField();
		monthlySalesField.setPromptText("Monthly sales");
		grid.addRow(6, monthlySalesLabel, monthlySalesField);

		Label workPreferenceLabel = new Label("Work preference");
		ChoiceBox<WorkPreference.PreferenceType> preferenceChoice = new AppChoiceBox<>(
				WorkPreference.PreferenceType.values());
		preferenceChoice.setOnAction(event -> ((EmployeeFormController) controller).onPreferenceChange());
		grid.addRow(7, workPreferenceLabel, preferenceChoice);

		Label hourChangeMainLabel = new Label("How many hours?");
		HBox hourChangeHbox = new HBox();
		hourChangeHbox.setAlignment(Pos.CENTER_LEFT);
		hourChangeHbox.setSpacing(10);
		TextField hourChangeField = new TextField();
		Label hourChangeLabel = new Label();
		hourChangeHbox.getChildren().addAll(hourChangeField, hourChangeLabel);
		grid.addRow(8, hourChangeMainLabel, hourChangeHbox);

		Text errorText = new Text();
		errorText.setFill(Color.RED);
		grid.addRow(9, errorText);
		GridPane.setColumnSpan(errorText, 2);
		GridPane.setHalignment(errorText, HPos.CENTER);

		((EmployeeFormController) controller).setNameField(nameField);
		((EmployeeFormController) controller).setIdField(idField);
		((EmployeeFormController) controller).setDepartmentChoice(departmentChoice);
		((EmployeeFormController) controller).setRoleChoice(roleChoice);
		((EmployeeFormController) controller).setSalaryChoice(salaryChoice);
		((EmployeeFormController) controller).setMonthlyHoursField(monthlyHoursField);
		((EmployeeFormController) controller).setMonthlySalesField(monthlySalesField);
		((EmployeeFormController) controller).setPreferenceChoice(preferenceChoice);
		((EmployeeFormController) controller).setHourChangeMainLabel(hourChangeMainLabel);
		((EmployeeFormController) controller).setHourChangeLabel(hourChangeLabel);
		((EmployeeFormController) controller).setHourChangeField(hourChangeField);
		((EmployeeFormController) controller).setErrorText(errorText);

		return grid;
	}
}
