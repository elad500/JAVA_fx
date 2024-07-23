package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class State {
    private static ObservableList<Department> departments = FXCollections.observableArrayList();
    private static ObservableList<Role> roles = FXCollections.observableArrayList();
    private static ObservableList<Employee> employees = FXCollections.observableArrayList();

    public static ObservableList<Department> getDepartments() {
        return departments;
    }
    public static ObservableList<Role> getRoles() {
        return roles;
    }
    public static ObservableList<Employee> getEmployees() {
        return employees;
    }
}
