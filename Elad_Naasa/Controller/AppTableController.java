package Controller;

import View.AppFormView;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AppTableController<T> {
  private TableView<T> dataTable;
  private AppFormView<T> formView;

  public void setDataTable(TableView<T> dataTable) {
    this.dataTable = dataTable;
  }

  public void setFormView(AppFormView<T> formView) {
    this.formView = formView;
  }

  public void add(Stage stage) {
    formView.load(stage);
  }

  public void edit(Stage stage) {
    ObservableList<T> selectedElements = dataTable.getSelectionModel().getSelectedItems();
    if (selectedElements.size() > 0) {
      T selectedElement = selectedElements.get(0);
      formView.load(stage, selectedElement);
    }
  }

  public void delete() {
    ObservableList<T> allElements, selectedElements;
    allElements = dataTable.getItems();
    selectedElements = dataTable.getSelectionModel().getSelectedItems();

    if (allElements.size() == 1 && selectedElements.size() == 1) {
      allElements.clear();
    } else {
      selectedElements.forEach(allElements::remove);
    }
  }

  public void back(Stage stage) {
    new View.MainScene().load(stage);
  }
}
