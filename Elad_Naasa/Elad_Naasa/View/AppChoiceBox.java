package View;

import Model.AppEnum;
import Model.CompanyEntity;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class AppChoiceBox<T> extends ChoiceBox<T> {

  private ChoiceBox<T> choiceBox;

  public AppChoiceBox(T[] values) {
    super();
    choiceBox = this;

    this.getItems().setAll(values);

    this.setConverter(new StringConverter<T>() {
      @Override
      public String toString(T appEnum) {
        return ((AppEnum)appEnum).getText();
      }
      @Override
      public T fromString(final String string) {
        return choiceBox.getItems().stream().filter(appEnum -> ((AppEnum)appEnum).getText()
            .equals(string)).findFirst().orElse(null);
      }
    });
  }

  public AppChoiceBox(ObservableList<T> list) {
    super();
    choiceBox = this;

    this.getItems().setAll(list);

    this.setConverter(new StringConverter<T>() {
      @Override
      public String toString(T data) {
        if(data == null) {
          return null;
        }
        return ((CompanyEntity)data).getName();
      }
      @Override
      public T fromString(final String string) {
        return choiceBox.getItems().stream().filter(data -> ((CompanyEntity)data).getName()
            .equals(string)).findFirst().orElse(null);
      }
    });
  }
}
