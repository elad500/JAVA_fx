package Controller;

import View.AppTableView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public abstract class AppFormController<T> {

  public enum FORM_TYPE {ADD, EDIT};
  public FORM_TYPE type;
  public T data;
  public AppTableView<T> tableView;
  public Label heading;

  public void setHeading(Label heading) {
    this.heading = heading;
  }

  public void setTableView(AppTableView<T> tableView) {
    this.tableView = tableView;
  }

  public void save(Stage stage) {
    if (validateForm()) {
      if (type == FORM_TYPE.ADD) {
        createNewEntry();
        showTableScene(stage);
      } else if (type == FORM_TYPE.EDIT) {
        updateExistingEntry();
        showTableScene(stage);
      }
    }
  }

  public abstract void setForm(FORM_TYPE type);

  public abstract void setForm(FORM_TYPE type, T data);

  public void cancel(Stage stage) {
    showTableScene(stage);
  }

  public void showTableScene(Stage stage) {
    tableView.load(stage);
  }

  abstract boolean validateForm();

  abstract void createNewEntry();

  abstract void updateExistingEntry();
}
