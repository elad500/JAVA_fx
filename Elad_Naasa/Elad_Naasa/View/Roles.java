package View;

import Controller.AppTableController;
import Model.Role;
import Model.State;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;

public class Roles extends AppTableView<Role> {

	public Roles() {
		super(State.getRoles(), new AppTableController<>());
	}

	@Override
	public void load(Stage stage) {
		try {
			this.stage = stage;
			formView = new RoleForm();
			Parent root = buildView();
			controller.setDataTable(dataTable);
			controller.setFormView(formView);
			Scene scene = new Scene(root, 900, 600);
			stage.setTitle("Optimize Business - Roles");
			stage.setScene(scene);
			stage.getScene().getStylesheets().add(getClass().getResource("stylesheets/table.css").toExternalForm());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	List<TableColumn> getTableColumns() {
		TableColumn<Role, String> nameColumn = new TableColumn<>("Title");
		TableColumn<Role, Number> employeeCountColumn = new TableColumn<>("# Employees");
		TableColumn<Role, String> allowHourChangeColumn = new TableColumn<>("Allow hour changes");
		TableColumn<Role, String> requireHourSyncColumn = new TableColumn<>("Require hours sync");
		TableColumn<Role, String> workPreferenceColumn = new TableColumn<>("Work Preference");
		TableColumn<Role, Number> hourChangeColumn = new TableColumn<>("Hour change");
		TableColumn<Role, Number> profitHours = new TableColumn<>("Profit (Hours)");
		TableColumn<Role, Number> profitNis = new TableColumn<>("Profit (NIS)");

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		employeeCountColumn
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEmployees().size()));
		allowHourChangeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				boolToStr(cellData.getValue().getWorkPolicy().getAllowHourChange())));
		requireHourSyncColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(boolToStr(cellData.getValue().getWorkPolicy().getRequireSync())));
		workPreferenceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getWorkPolicy().getCurrentPolicy().getPreferenceType().getText()));
		hourChangeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
				cellData.getValue().getWorkPolicy().getCurrentPolicy().getHourChange()));
		profitHours.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().calculateCompanyProfitInHours()));
		profitNis.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().calculateCompanyProfitInNis()));

		return Arrays.asList(nameColumn, employeeCountColumn, allowHourChangeColumn, requireHourSyncColumn,
				workPreferenceColumn, hourChangeColumn, profitHours, profitNis);
	}
}
