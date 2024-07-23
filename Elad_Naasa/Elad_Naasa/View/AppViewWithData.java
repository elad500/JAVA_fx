package View;

import javafx.stage.Stage;

public interface AppViewWithData<T> {
  void load(Stage stage, T data);
}

