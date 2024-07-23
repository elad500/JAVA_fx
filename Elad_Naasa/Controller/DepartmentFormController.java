package Controller;

import Model.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class DepartmentFormController extends AppFormController<Department> {
//
 private TextField nameField;
 private CheckBox requireHourSync;
 private Label hourChangeMainLabel;
 private Label hourChangeLabel;
 private TextField hourChangeField;
 private CheckBox allowHourChange;
 private Label workPreferenceLabel;
 private ChoiceBox<WorkPreference.PreferenceType> preferenceChoice;
 private Text errorText;

  public void setNameField(TextField nameField) {
    this.nameField = nameField;
  }

  public void setRequireHourSync(CheckBox requireHourSync) {
    this.requireHourSync = requireHourSync;
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

  public void setAllowHourChange(CheckBox allowHourChange) {
    this.allowHourChange = allowHourChange;
  }

  public void setWorkPreferenceLabel(Label workPreferenceLabel) {
    this.workPreferenceLabel = workPreferenceLabel;
  }

  public void setPreferenceChoice(ChoiceBox<WorkPreference.PreferenceType> preferenceChoice) {
    this.preferenceChoice = preferenceChoice;
  }

  public void setErrorText(Text errorText) {
    this.errorText = errorText;
  }

  public void onPreferenceChange() {
    setHourChangeField();
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

  public void onAllowHourChangeClick() {
    if (allowHourChange.isSelected()) {
      workPreferenceLabel.setVisible(true);
      preferenceChoice.setVisible(true);
    } else {
      workPreferenceLabel.setVisible(false);
      preferenceChoice.setVisible(false);
      preferenceChoice.setValue(WorkPreference.PreferenceType.NO_CHANGE);
      hourChangeField.setText("0");
    }
  }

  public void setForm(FORM_TYPE type) {
    setFormType(type);
    workPreferenceLabel.setVisible(false);
    preferenceChoice.setVisible(false);
    preferenceChoice.setValue(WorkPreference.PreferenceType.NO_CHANGE);
  }

  public void setForm(FORM_TYPE type, Department department) {
    setFormType(type);
    data = department;
    nameField.setText(department.getName());
    allowHourChange.setSelected(department.getWorkPolicy().getAllowHourChange());
    requireHourSync.setSelected(department.getWorkPolicy().getRequireSync());
    preferenceChoice.setValue(department.getWorkPolicy().getCurrentPolicy().getPreferenceType());
    hourChangeField.setText(Integer.toString(department.getWorkPolicy().getCurrentPolicy().getHourChange()));
    if(!department.getWorkPolicy().getAllowHourChange()) {
      workPreferenceLabel.setVisible(false);
      preferenceChoice.setVisible(false);
    }
  }

  public void setFormType(FORM_TYPE type) {
    this.type = type;
    if (type == FORM_TYPE.ADD) {
      heading.setText("Add Department");
    } else {
      heading.setText("Edit Department");
    }
  }

  boolean validateForm() {
    resetFieldErrorState();
    return validateName() && validateNotExists() && validateHourChange();
  }

  @Override
  void createNewEntry() {
    int hourChangeStr = !WorkPreference.isHourChangePreference(preferenceChoice.getValue()) ||
        "".equals(hourChangeField.getText()) ? 0 : Integer.parseInt(hourChangeField.getText());
    WorkPreference preference = new WorkPreference(preferenceChoice.getValue(), hourChangeStr);
    WorkPolicy workPolicy = new WorkPolicy(allowHourChange.isSelected(), requireHourSync.isSelected(), preference);
    Department department = new Department(nameField.getText(), workPolicy);
    State.getDepartments().add(department);
  }

  @Override
  void updateExistingEntry() {
    data.setName(nameField.getText());
    data.getWorkPolicy().setAllowHourChange(allowHourChange.isSelected());
    data.getWorkPolicy().setRequireSync(requireHourSync.isSelected());
    data.getWorkPolicy().getCurrentPolicy().setPreferenceType(preferenceChoice.getValue());
    int hourChangeStr = !WorkPreference.isHourChangePreference(preferenceChoice.getValue()) ||
        "".equals(hourChangeField.getText()) ? 0 : Integer.parseInt(hourChangeField.getText());
    data.getWorkPolicy().getCurrentPolicy().setHourChange(hourChangeStr);
  }

  private boolean validateName() {
    if (nameField.getText().trim().length() == 0) {
      setControlError(nameField, "Name can't be empty");
      return false;
    }
    return true;
  }

  private boolean validateNotExists() {
    for (Department item : State.getDepartments()) {
      if (((type == FORM_TYPE.ADD && nameField.getText().equals(item.getName())) ||
          (type == FORM_TYPE.EDIT && item != data && nameField.getText().equals(item.getName())))) {
        setControlError(nameField, "Department already exists");
        return false;
      }
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
    hourChangeField.setStyle("-fx-border-color: none;");
  }

}