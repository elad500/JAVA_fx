package Controller;

import Model.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class RoleFormController extends AppFormController<Role> {

  TextField nameField;
  CheckBox requireHourSync;
  Label hourChangeMainLabel;
  Label hourChangeLabel;
  TextField hourChangeField;
  CheckBox allowHourChange;
  Label workPreferenceLabel;
  ChoiceBox<WorkPreference.PreferenceType> preferenceChoice;
  Text errorText;

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

  @Override
  public void setForm(FORM_TYPE type) {
    setFormType(type);
    this.workPreferenceLabel.setVisible(false);
    this.preferenceChoice.setVisible(false);
    this.preferenceChoice.setValue(WorkPreference.PreferenceType.NO_CHANGE);
  }

  @Override
  public void setForm(FORM_TYPE type, Role role) {
    setFormType(type);
    data = role;
    this.nameField.setText(role.getName());
    this.allowHourChange.setSelected(role.getWorkPolicy().getAllowHourChange());
    this.requireHourSync.setSelected(role.getWorkPolicy().getRequireSync());
    this.preferenceChoice.setValue(role.getWorkPolicy().getCurrentPolicy().getPreferenceType());
    this.hourChangeField.setText(Integer.toString(role.getWorkPolicy().getCurrentPolicy().getHourChange()));
    if(!role.getWorkPolicy().getAllowHourChange()) {
      this.workPreferenceLabel.setVisible(false);
      this.preferenceChoice.setVisible(false);
    }
  }

  public void setFormType(FORM_TYPE type) {
    this.type = type;
    if (type == FORM_TYPE.ADD) {
      this.heading.setText("Add Role");
    } else {
      this.heading.setText("Edit Role");
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
    Role role = new Role(nameField.getText(), workPolicy);
    State.getRoles().add(role);
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
      setControlError(nameField, "Title can't be empty");
      return false;
    }
    return true;
  }

  private boolean validateNotExists() {
    for (Role item : State.getRoles()) {
      if (((type == FORM_TYPE.ADD && nameField.getText().equals(item.getName())) ||
          (type == FORM_TYPE.EDIT && item != data && nameField.getText().equals(item.getName())))) {
        setControlError(nameField, "Role already exists");
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