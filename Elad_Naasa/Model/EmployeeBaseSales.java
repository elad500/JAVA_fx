package Model;


import Common.Config;

public class EmployeeBaseSales extends Employee {

  private int monthlySales;

  public EmployeeBaseSales(int monthlySales) {
    this.monthlySales = monthlySales;
  }

  public int getMonthlySales() {
    return monthlySales;
  }

  @Override
  public double calculateCompanyProfitInNis() {
    double companyProfitInHours = calculateCompanyProfitInHours();
    return companyProfitInHours * Config.COMPANY_PROFIT_PER_EMPLOYEE_PER_HOUR
        + (monthlySales / ((double)Config.EMPLOYEE_HOURS_PER_MONTH * Config.DAILY_WORK_HOURS)) * companyProfitInHours;
  }

  @Override
  public String writeCsvRow() {
    StringBuilder builder = new StringBuilder(writeBasicCsvRow()).append(Config.CSV_SEPARATOR);
    builder.append(monthlySales).append(Config.CSV_NEW_LINE);
    return builder.toString();
  }

}