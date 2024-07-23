package View;

import Controller.RoleFormController;
import Model.Role;
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

public class RoleForm extends AppFormView<Role> {

  public RoleForm() {
    super(new RoleFormController());
  }

  @Override
  public void load(Stage stage) {
    try {
      this.stage = stage;
      Parent root = buildView();
      tableView = new Roles();
      controller.setTableView(tableView);
      controller.setForm(RoleFormController.FORM_TYPE.ADD);
      setTableView(tableView);
      Scene scene = new Scene(root,600,600);
      stage.setTitle("Optimize Business - Add Role");
      stage.setScene(scene);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void load(Stage stage, Role role) {
    try {
      this.stage = stage;
      tableView = new Roles();
      Parent root = buildView();
      controller.setTableView(tableView);
      controller.setForm(RoleFormController.FORM_TYPE.EDIT, role);
      setTableView(tableView);
      Scene scene = new Scene(root,600,600);
      stage.setTitle("Optimize Business - Edit Role");
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

    Label nameLabel = new Label("Title");
    TextField nameField = new TextField();
    nameField.setPromptText("Role title");
    grid.addRow(0, nameLabel, nameField);

    Label allowHourChangeLabel = new Label("Allow change of working hours");
    CheckBox allowHourChange = new CheckBox();
    allowHourChange.setOnAction(event -> ((RoleFormController)controller).onAllowHourChangeClick());
    grid.addRow(1, allowHourChangeLabel, allowHourChange);

    Label hourSyncLabel = new Label("Require hours sync");
    CheckBox requireHourSync = new CheckBox();
    grid.addRow(2, hourSyncLabel, requireHourSync);

    Label workPreferenceLabel = new Label("Work preference");
    ChoiceBox<WorkPreference.PreferenceType> preferenceChoice = new AppChoiceBox<>(WorkPreference.PreferenceType.values());
    preferenceChoice.setOnAction(event -> ((RoleFormController)controller).onPreferenceChange());
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

    ((RoleFormController)controller).setNameField(nameField);
    ((RoleFormController)controller).setRequireHourSync(requireHourSync);
    ((RoleFormController)controller).setHourChangeMainLabel(hourChangeMainLabel);
    ((RoleFormController)controller).setHourChangeLabel(hourChangeLabel);
    ((RoleFormController)controller).setHourChangeField(hourChangeField);
    ((RoleFormController)controller).setAllowHourChange(allowHourChange);
    ((RoleFormController)controller).setWorkPreferenceLabel(workPreferenceLabel);
    ((RoleFormController)controller).setPreferenceChoice(preferenceChoice);
    ((RoleFormController)controller).setErrorText(errorText);

    return grid;
  }
}
