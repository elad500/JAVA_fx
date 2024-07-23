package Controller;

import View.Departments;
import View.Employees;
import View.Roles;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class MainSceneController {

  public void showDepartments(Stage stage) {
    new Departments().load(stage);
  }

  public void showRoles(Stage stage) {
    new Roles().load(stage);
  }

  public void showEmployees(Stage stage) {
    new Employees().load(stage);
  }

//  public void showProfitScene(ActionEvent actionEvent) {
//  }
}
