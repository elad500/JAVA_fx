package View;

import Controller.MainSceneController;
import Model.Persistence;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainScene extends Application implements AppView {

  MainSceneController controller;
  Stage stage;

  @Override
  public void start(Stage stage) {
    loadDataOnAppStart();
    load(stage);
  }

  @Override
  public void load(Stage stage) {
    try {
      this.stage = stage;
      stage.setOnCloseRequest(e -> saveDataOnAppClose());
      Parent root = buildView();
      controller = new MainSceneController();
      Scene scene = new Scene(root,400,500);
      stage.setTitle("Optimize Business");
      stage.setScene(scene);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Parent buildView() {
    VBox vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(15);

    Button departments = new Button("Departments");
    Button roles = new Button("Roles");
    Button employees = new Button("Employees");

    departments.setOnAction(event -> controller.showDepartments(stage));
    roles.setOnAction(event -> controller.showRoles(stage));
    employees.setOnAction(event -> controller.showEmployees(stage));

    vBox.getChildren().addAll(departments, roles, employees);

    return vBox;
  }

  private void saveDataOnAppClose() {
    try {
      Persistence.saveData();
    } catch (IOException ioException) {
      ioException.printStackTrace();
      System.out.println("Error while saving data");
    }
  }

  private void loadDataOnAppStart() {
    try {
      Persistence.loadData();
    } catch (IOException e) {
      System.out.println("Error opening data file");
      e.printStackTrace();
    }
  }

}
