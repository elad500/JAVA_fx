package Model;

public class EmployeeHours extends Employee {

  public EmployeeHours(int monthlyHours) {
    super();
    this.monthlyHours = monthlyHours;
  }

  public int getMonthlyHours() {
    return monthlyHours;
  }

}
