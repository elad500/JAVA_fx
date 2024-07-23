package View;

import Controller.DepartmentFormController;
import Model.WorkPreference;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Model.Department;

public class DepartmentForm extends AppFormView<Department> {

  public DepartmentForm() {
    super(new DepartmentFormController());
  }

  @Override
  public void load(Stage stage) {
    try {
      this.stage = stage;
      Parent root = buildView();
      tableView = new Departments();
      controller.setTableView(tableView);
      controller.setForm(DepartmentFormController.FORM_TYPE.ADD);
      setTableView(tableView);
      Scene scene = new Scene(root,600,600);
      stage.setTitle("Optimize Business - Add Department");
      stage.setScene(scene);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void load(Stage stage, Department department) {
    try {
      this.stage = stage;
      tableView = new Departments();
      Parent root = buildView();
      controller.setTableView(tableView);
      controller.setForm(DepartmentFormController.FORM_TYPE.EDIT, department);
      setTableView(tableView);
      Scene scene = new Scene(root,600,600);
      stage.setTitle("Optimize Business - Edit Department");
      stage.setScene(scene);
      stage.show();
    } catch(Exception e) {
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

    Label nameLabel = new Label("Name");
    TextField nameField = new TextField();
    nameField.setPromptText("Department name");
    grid.addRow(0, nameLabel, nameField);

    Label allowHourChangeLabel = new Label("Allow change of working hours");
    CheckBox allowHourChange = new CheckBox();
    allowHourChange.setOnAction(event -> ((DepartmentFormController)controller).onAllowHourChangeClick());
    grid.addRow(1, allowHourChangeLabel, allowHourChange);

    Label hourSyncLabel = new Label("Require hours sync");
    CheckBox requireHourSync = new CheckBox();
    grid.addRow(2, hourSyncLabel, requireHourSync);

    Label workPreferenceLabel = new Label("Work preference");
    ChoiceBox<WorkPreference.PreferenceType> preferenceChoice = new AppChoiceBox<>(WorkPreference.PreferenceType.values());
    preferenceChoice.setOnAction(event -> ((DepartmentFormController)controller).onPreferenceChange());
    grid.addRow(3, workPreferenceLabel, preferenceChoice);

    Label hourChangeMainLabel = new Label("How many hours?");
    HBox hourChangeHbox = new HBox();
    hourChangeHbox.setAlignment(Pos.CENTER_LEFT);
    hourChangeHbox.setSpacing(10);
    TextField hourChangeField = new TextField();
    Label hourChangeLabel = new Label();
    hourChangeHbox.getChildren().addAll(hourChangeField, hourChangeLabel);
    grid.addRow(4, hourChangeMainLabel, hourChangeHbox);

    Text errorText = new Text();
    errorText.setFill(Color.RED);
    grid.addRow(5, errorText);
    GridPane.setColumnSpan(errorText, 2);
    GridPane.setHalignment(errorText, HPos.CENTER);

    ((DepartmentFormController)controller).setNameField(nameField);
    ((DepartmentFormController)controller).setRequireHourSync(requireHourSync);
    ((DepartmentFormController)controller).setHourChangeMainLabel(hourChangeMainLabel);
    ((DepartmentFormController)controller).setHourChangeLabel(hourChangeLabel);
    ((DepartmentFormController)controller).setHourChangeField(hourChangeField);
    ((DepartmentFormController)controller).setAllowHourChange(allowHourChange);
    ((DepartmentFormController)controller).setWorkPreferenceLabel(workPreferenceLabel);
    ((DepartmentFormController)controller).setPreferenceChoice(preferenceChoice);
    ((DepartmentFormController)controller).setErrorText(errorText);

    return grid;
  }

}
