package View;

import Controller.AppTableController;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public abstract class AppTableView<T> implements AppView {

	AppTableController<T> controller;
	ObservableList<T> model;
	AppFormView<T> formView;
	Stage stage;
	TableView<T> dataTable;

	public AppTableView(ObservableList<T> model, AppTableController<T> controller) {
		this.model = model;
		this.controller = controller;
	}

	@Override
	public Parent buildView() {
		VBox vBox = new VBox();
		vBox.setPrefSize(900, 600);

		dataTable = setTable();
		HBox controls = setControls();

		vBox.getChildren().addAll(dataTable, controls);

		return vBox;
	}

	TableView<T> setTable() {
		TableView<T> table = new TableView<>();
		table.setPrefSize(900, 500);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		for (TableColumn column : getTableColumns()) {
			table.getColumns().add(column);
		}

		table.setItems(model);
		return table;
	}

	abstract List<TableColumn> getTableColumns();

	HBox setControls() {
		HBox controls = new HBox();
		controls.setPrefSize(900, 100);
		controls.setAlignment(Pos.CENTER);
		controls.setSpacing(30);

		Button add = new Button("Add");
		Button edit = new Button("Edit");
		Button delete = new Button("Delete");
		Button back = new Button("Back");

		add.setOnAction(event -> controller.add(stage));
		edit.setOnAction(event -> controller.edit(stage));
		delete.setOnAction(event -> controller.delete());
		back.setOnAction(event -> controller.back(stage));

		controls.getChildren().add(add);
		controls.getChildren().add(edit);
		controls.getChildren().add(delete);
		controls.getChildren().add(back);

		return controls;
	}

	String boolToStr(boolean bool) {
		return bool ? "Yes" : "No";
	}

}
