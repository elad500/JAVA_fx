package Controller;

import Common.Config;
import Model.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class EmployeeFormController extends AppFormController<Employee> {
//
  private TextField nameField;
  private TextField idField;
  private ChoiceBox<Role> roleChoice;
  private ChoiceBox<Department> departmentChoice;
  private ChoiceBox<Employee.SalaryType> salaryChoice;
  private ChoiceBox<WorkPreference.PreferenceType> preferenceChoice;
  private TextField hourChangeField;
  private Label hourChangeLabel;
  private Label hourChangeMainLabel;
  private TextField monthlyHoursField;
  private TextField monthlySalesField;
  private Text errorText;

  public void setNameField(TextField nameField) {
    this.nameField = nameField;
  }

  public void setIdField(TextField idField) {
    this.idField = idField;
  }

  public void setDepartmentChoice(ChoiceBox<Department> departmentChoice) {
    this.departmentChoice = departmentChoice;
  }

  public void setRoleChoice(ChoiceBox<Role> roleChoice) {
    this.roleChoice = roleChoice;
  }

  public void setSalaryChoice(ChoiceBox<Employee.SalaryType> salaryChoice) {
    this.salaryChoice = salaryChoice;
  }

  public void setPreferenceChoice(ChoiceBox<WorkPreference.PreferenceType> preferenceChoice) {
    this.preferenceChoice = preferenceChoice;
  }

  public void setHourChangeMainLabel(Label hourChangeMainLabel) {
    this.hourChangeMainLabel = hourChangeMainLabel;
  }

  public void setHourChangeLabel(Label hourChangeLabel) {
    this.hourChangeLabel = hourChangeLabel;
  }

  public void setHourChangeField(TextField hourChangeField) {
    this.hourChangeField = hourChangeField;
  }

  public void setMonthlyHoursField(TextField monthlyHoursField) {
    this.monthlyHoursField = monthlyHoursField;
  }

  public void setMonthlySalesField(TextField monthlySalesField) {
    this.monthlySalesField = monthlySalesField;
  }

  public void setErrorText(Text errorText) {
    this.errorText = errorText;
  }

  public void onPreferenceChange() {
    setHourChangeField();
  }

  public void onSalaryChange() {
	  setSalaryFields();
	  }

  private void setSalaryFields() {
    Employee.SalaryType salaryType = salaryChoice.getValue();
    if (salaryType == Employee.SalaryType.BASE) {
      monthlyHoursField.setDisable(true);
      monthlySalesField.setDisable(true);
      monthlyHoursField.setText("");
      monthlySalesField.setText("");
    } else if (salaryType == Employee.SalaryType.BASE_PLUS_SALES) {
      monthlyHoursField.setDisable(true);
      monthlySalesField.setDisable(false);
      monthlyHoursField.setText("");
    } else {
      monthlyHoursField.setDisable(false);
      monthlySalesField.setDisable(true);
      monthlySalesField.setText("");
    }
  }

  private void setHourChangeField() {
    WorkPreference.PreferenceType type = preferenceChoice.getValue();
    if (WorkPreference.isHourChangePreference(type)) {
      if (type == WorkPreference.PreferenceType.EARLY) {
        hourChangeLabel.setText("hours earlier");
      } else {
        hourChangeLabel.setText("hours later");
      }
      hourChangeField.setVisible(true);
      hourChangeMainLabel.setVisible(true);
      hourChangeLabel.setVisible(true);
    } else {
      hourChangeField.setVisible(false);
      hourChangeMainLabel.setVisible(false);
      hourChangeLabel.setVisible(false);
    }
  }

  public void setFormType(FORM_TYPE type) {
    this.type = type;
    if (type == FORM_TYPE.ADD) {
      heading.setText("Add Employee");
    } else {
      heading.setText("Edit Employee");
    }
  }
//
  @Override
  public void setForm(FORM_TYPE type) {
    setFormType(type);
    this.salaryChoice.setValue(Employee.SalaryType.BASE);
    this.preferenceChoice.setValue(WorkPreference.PreferenceType.NO_CHANGE);
  }

  @Override
  public void setForm(FORM_TYPE type, Employee employee) {
    setFormType(type);
    data = employee;
    nameField.setText(employee.getFullName());
    idField.setText(Integer.toString(employee.getId()));
    roleChoice.setValue(employee.getRole());
    departmentChoice.setValue(employee.getDepartment());
    salaryChoice.setValue(employee.getSalaryType());
    preferenceChoice.setValue(employee.getWorkPreference().getPreferenceType());
    hourChangeField.setText(Integer.toString(employee.getWorkPreference().getHourChange()));
    if (employee instanceof EmployeeHours) {
      monthlyHoursField.setText(Integer.toString(((EmployeeHours)employee).getMonthlyHours()));
    } else if (employee instanceof EmployeeBaseSales) {
      monthlySalesField.setText(Integer.toString(((EmployeeBaseSales)employee).getMonthlySales()));
    }
  }



  @Override
  void createNewEntry() {
    int monthlyHours = "".equals(monthlyHoursField.getText()) ?
        Config.EMPLOYEE_HOURS_PER_MONTH : Integer.parseInt(monthlyHoursField.getText());
    int monthlySales = "".equals(monthlySalesField.getText()) ? 0 : Integer.parseInt(monthlySalesField.getText());
    Employee employee = EmployeeBuilder.build(salaryChoice.getValue(), monthlyHours, monthlySales);

    employee.setFullName(nameField.getText());
    employee.setId(Integer.parseInt(idField.getText()));
    employee.setDepartment(departmentChoice.getValue());
    employee.setRole(roleChoice.getValue());
    employee.setSalaryType(salaryChoice.getValue());
    String hourChangeStr = !WorkPreference.isHourChangePreference(preferenceChoice.getValue()) ||
        "".equals(hourChangeField.getText()) ? "0" : hourChangeField.getText();
    WorkPreference workPreference = new WorkPreference(preferenceChoice.getValue(),
        Integer.parseInt(hourChangeStr));
    employee.setWorkPreference(workPreference);

    State.getEmployees().add(employee);
    employee.getDepartment().getEmployees().add(employee);
    employee.getRole().getEmployees().add(employee);
  }

  @Override
  void updateExistingEntry() {
    Department currentDepartment = data.getDepartment();
    Role currentRole = data.getRole();

    currentDepartment.getEmployees().remove(data);
    currentRole.getEmployees().remove(data);
    State.getEmployees().remove(data);

    createNewEntry();
  }

  boolean validateForm() {
    resetFieldErrorState();
    return validateName() && validateId() && validateNotExists() && validateDepartment()
        && validateRole() && validateMonthlyHours() && validateMonthlySales()
        && validateHourChange();
  }

  private boolean validateName() {
    if (nameField.getText().trim().length() == 0) {
      setControlError(nameField, "Name can't be empty");
      return false;
    }
    return true;
  }

  private boolean validateId() {
    String idStr = idField.getText();
    if (idStr.length() == 0) {
      setControlError(idField, "ID can't be empty");
      return false;
    }
    try {
      int id = Integer.parseInt(idStr);
      if (id <= 0) {
        setControlError(idField, "Please enter a valid id number");
        return false;
      }
    } catch (NumberFormatException e) {
      setControlError(idField, "Please enter a valid id number");
      return false;
    }
    return true;
  }

  private boolean validateMonthlyHours() {
    if (salaryChoice.getValue() != Employee.SalaryType.HOURS) {
      return true;
    }
    String monthlyHoursStr = monthlyHoursField.getText();
    if (monthlyHoursStr.length() == 0) {
      setControlError(monthlyHoursField, "Monthly hours can't be empty");
      return false;
    }
    try {
      int hours = Integer.parseInt(monthlyHoursStr);
      if (hours <= 0) {
        setControlError(monthlyHoursField, "Please enter a positive number of hours");
        return false;
      }
    } catch (NumberFormatException e) {
      setControlError(monthlyHoursField, "Please enter a positive number of hours");
      return false;
    }
    return true;
  }

  private boolean validateMonthlySales() {
    if (salaryChoice.getValue() != Employee.SalaryType.BASE_PLUS_SALES) {
      return true;
    }
    String monthlySalesStr = monthlySalesField.getText();
    if (monthlySalesStr.length() == 0) {
      setControlError(monthlySalesField, "Monthly sales can't be empty");
      return false;
    }
    try {
      int hours = Integer.parseInt(monthlySalesStr);
      if (hours <= 0) {
        setControlError(monthlySalesField, "Please enter a positive number for monthly sales");
        return false;
      }
    } catch (NumberFormatException e) {
      setControlError(monthlySalesField, "Please enter a positive number for monthly sales");
      return false;
    }
    return true;
  }

  private boolean validateNotExists() {
    int id = Integer.parseInt(idField.getText());
    for (Employee item : State.getEmployees()) {
      if (((type == FORM_TYPE.ADD && id == item.getId()) ||
          (type == FORM_TYPE.EDIT && item != data && id == item.getId()))) {
        setControlError(idField, "Employee already exists");
        return false;
      }
    }
    return true;
  }

  private boolean validateDepartment() {
    if (departmentChoice.getValue() == null) {
      setControlError(departmentChoice, "You must choose a department");
      return false;
    }
    return true;
  }

  private boolean validateRole() {
    if (roleChoice.getValue() == null) {
      setControlError(roleChoice, "You must choose a role");
      return false;
    }
    return true;
  }

  private boolean validateHourChange() {
    if (!WorkPreference.isHourChangePreference(preferenceChoice.getValue())) {
      return true;
    }
    String hourChangeStr = hourChangeField.getText();
    if (hourChangeStr.length() == 0) {
      setControlError(hourChangeField, "Number of hours can't be empty");
      return false;
    }
    try {
      int hourChange = Integer.parseInt(hourChangeStr);
      if (hourChange <= 0 || hourChange > 7) {
        setControlError(hourChangeField, "Number of hours must be in range [1,7]");
        return false;
      }
    } catch (NumberFormatException e) {
      setControlError(hourChangeField, "Please fill a valid number of hours");
      return false;
    }
    return true;
  }

  private void setControlError(Control field, String message) {
    field.setStyle("-fx-border-color: red;");
    errorText.setText(message);
  }

  private void resetFieldErrorState() {
    nameField.setStyle("-fx-border-color: none;");
    idField.setStyle("-fx-border-color: none;");
    hourChangeField.setStyle("-fx-border-color: none;");
    departmentChoice.setStyle("-fx-border-color: none;");
    roleChoice.setStyle("-fx-border-color: none;");
    monthlyHoursField.setStyle("-fx-border-color: none;");
    monthlySalesField.setStyle("-fx-border-color: none;");
  }

}