package View;

import Controller.AppFormController;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public abstract class AppFormView<T> implements AppView, AppViewWithData<T>{
  AppFormController<T> controller;
  AppTableView<T> tableView;
  Stage stage;

  public AppFormView(AppFormController<T> controller) {
    this.controller = controller;
  }

  public void setTableView(AppTableView<T> tableView) {
    this.tableView = tableView;
  }

  @Override
  public Parent buildView() {
    VBox vBox = new VBox();
    vBox.setPrefSize(600,600);
    vBox.setSpacing(30);
    vBox.setAlignment(Pos.CENTER);

    Label heading = new Label();
    heading.setStyle("-fx-font-size: 40; -fx-font-weight: bold;");

    controller.setHeading(heading);

    GridPane grid = setGrid();
    HBox controls = setControls();

    vBox.getChildren().addAll(heading, grid, controls);
    return vBox;
  }

  abstract GridPane setGrid();

  HBox setControls() {
    HBox controls = new HBox();
    controls.setPrefSize(600,100);
    controls.setAlignment(Pos.CENTER);
    controls.setSpacing(30);

    Button save = new Button("Save");
    Button cancel = new Button("Cancel");

    save.setOnAction(event -> controller.save(stage));
    cancel.setOnAction(event -> controller.cancel(stage));

    controls.getChildren().add(save);
    controls.getChildren().add(cancel);

    return controls;
  }

}
