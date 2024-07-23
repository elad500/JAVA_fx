package View;

import javafx.scene.Parent;
import javafx.stage.Stage;

public interface AppView {
  void load(Stage stage);

  Parent buildView();
}
