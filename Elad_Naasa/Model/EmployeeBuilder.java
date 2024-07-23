package Model;

public class EmployeeBuilder {

	public static Employee build(Employee.SalaryType salaryType, int monthlyHours, int monthlySales) {
    switch (salaryType) {
      case BASE:   
      return new EmployeeBase(); 
      case BASE_PLUS_SALES: 
      return new EmployeeBaseSales(monthlySales); 
      case HOURS: 
      return new EmployeeHours(monthlyHours); 
      default:  
      return new EmployeeBase(); 
    }
  }

}
