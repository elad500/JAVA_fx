package Model;

import Common.Config;

public abstract class Employee implements CompanyProfit {

	protected String fullName;
	protected int id;
	protected Department department;
	protected Role role;
	protected WorkPreference workPreference;
	protected SalaryType salaryType;
	protected int monthlyHours = Config.EMPLOYEE_HOURS_PER_MONTH;

	public enum SalaryType implements AppEnum {
		BASE("Base salary"), BASE_PLUS_SALES("Base salary + monthly sales"), HOURS("Hourly salary");

		private final String text;

		SalaryType(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}
	}

	public String getFullName() {
		return fullName;
	}

	public int getId() {
		return id;
	}

	public Department getDepartment() {
		return department;
	}

	public Role getRole() {
		return role;
	}

	public WorkPreference getWorkPreference() {
		return workPreference;
	}

	public SalaryType getSalaryType() {
		return salaryType;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setSalaryType(SalaryType salaryType) {
		this.salaryType = salaryType;
	}

	public void setWorkPreference(WorkPreference workPreference) {
		this.workPreference = workPreference;
	}

	@Override
	public double calculateCompanyProfitInHours() {
		WorkPreference appliedPreference = getPreferenceToApply();
		if (appliedPreference.getPreferenceType() == WorkPreference.PreferenceType.NO_CHANGE) {
			return 0;
		}
		double efficiencyMonthlyGain = calculateEfficiencyMonthlyProfitInHours(appliedPreference);
		double inefficiencyMonthlyLoss = calculateInefficiencyMonthlyProfitInHours(appliedPreference);
		return efficiencyMonthlyGain - inefficiencyMonthlyLoss;
	}

	@Override
	public double calculateCompanyProfitInNis() {
		return calculateCompanyProfitInHours() * Config.COMPANY_PROFIT_PER_EMPLOYEE_PER_HOUR;
	}

	private WorkPreference getPreferenceToApply() {
		if (department.getWorkPolicy().getCurrentPolicy()
				.getPreferenceType() != WorkPreference.PreferenceType.NO_CHANGE) {
			return department.getWorkPolicy().getCurrentPolicy();
		} else {
			return role.getWorkPolicy().getCurrentPolicy();
		}
	}

	private double calculateEfficiencyMonthlyProfitInHours(WorkPreference appliedPreference) {
		if (appliedPreference.getPreferenceType() == WorkPreference.PreferenceType.HOME) {
			if (workPreference.getPreferenceType() == WorkPreference.PreferenceType.HOME) {
				return monthlyHours * Config.HOME_EFFICIENCY_INCREASE;
			} else {
				return 0;
			}
		}

		int numberOfWorkDaysInMonth = monthlyHours / Config.DAILY_WORK_HOURS;
		int hourRemainder = monthlyHours % Config.DAILY_WORK_HOURS;

		if (appliedPreference.getPreferenceType() == workPreference.getPreferenceType()) {
			int effectiveDailyHours = Integer.min(appliedPreference.getHourChange(), workPreference.getHourChange());
			return (numberOfWorkDaysInMonth * effectiveDailyHours + Integer.min(effectiveDailyHours, hourRemainder))
					* Config.HOUR_EFFICIENCY_INCREASE;
		}
		return 0;
	}

	private double calculateInefficiencyMonthlyProfitInHours(WorkPreference appliedPreference) {
		if (appliedPreference.getPreferenceType() == WorkPreference.PreferenceType.HOME) {
			return 0;
		}

		int numberOfWorkDaysInMonth = monthlyHours / Config.DAILY_WORK_HOURS;
		if (appliedPreference.getPreferenceType() == workPreference.getPreferenceType()) {
			int ineffectiveDailyHours = Math.abs(appliedPreference.getHourChange() - workPreference.getHourChange());
			return (numberOfWorkDaysInMonth * ineffectiveDailyHours + getIneffectiveRemainder(ineffectiveDailyHours))
					* Config.HOUR_EFFICIENCY_DECREASE;
		}
		// applied is not HOME/NO_CHANGE and it doesn't agree with the employee's
		// preference
		else {
			int ineffectiveDailyHours = appliedPreference.getHourChange() + workPreference.getHourChange();
			return (numberOfWorkDaysInMonth * ineffectiveDailyHours + getIneffectiveRemainder(ineffectiveDailyHours))
					* Config.HOUR_EFFICIENCY_DECREASE;
		}
	}

	private int getIneffectiveRemainder(int ineffectiveDailyHours) {
		int hourRemainderToWork = monthlyHours % Config.DAILY_WORK_HOURS;
		int hoursThatCanBeSkipped = Config.DAILY_WORK_HOURS - hourRemainderToWork;
		return hoursThatCanBeSkipped >= ineffectiveDailyHours ? 0 : ineffectiveDailyHours - hoursThatCanBeSkipped;
	}

	public String writeBasicCsvRow() {
		StringBuilder builder = new StringBuilder();
		builder.append("Employee").append(Config.CSV_SEPARATOR);
		builder.append(fullName).append(Config.CSV_SEPARATOR);
		builder.append(id).append(Config.CSV_SEPARATOR);
		builder.append(department.getName()).append(Config.CSV_SEPARATOR);
		builder.append(role.getName()).append(Config.CSV_SEPARATOR);
		builder.append(workPreference.getPreferenceType()).append(Config.CSV_SEPARATOR);
		builder.append(workPreference.getHourChange()).append(Config.CSV_SEPARATOR);
		builder.append(salaryType).append(Config.CSV_SEPARATOR);
		builder.append(monthlyHours);
		return builder.toString();
	}

	public String writeCsvRow() {
		return writeBasicCsvRow() + Config.CSV_NEW_LINE;
	}

	public static void createFromCsvRow(String[] splitLine) {
		try {
			int monthlyHours = Integer.parseInt(splitLine[8]);
			int monthlySales = splitLine.length < 10 ? 0 : Integer.parseInt(splitLine[9]);
			Employee employee = EmployeeBuilder.build(SalaryType.valueOf(splitLine[7]), monthlyHours, monthlySales);
			Department department = State.getDepartments().stream().filter(elem -> splitLine[3].equals(elem.getName()))
					.findAny().orElse(null);
			Role role = State.getRoles().stream().filter(elem -> splitLine[4].equals(elem.getName())).findAny()
					.orElse(null);

			employee.setFullName(splitLine[1]);
			employee.setId(Integer.parseInt(splitLine[2]));
			employee.setDepartment(department);
			employee.setRole(role);
			employee.setSalaryType(SalaryType.valueOf(splitLine[7]));
			WorkPreference workPreference = new WorkPreference(WorkPreference.PreferenceType.valueOf(splitLine[5]),
					Integer.parseInt(splitLine[6]));
			employee.setWorkPreference(workPreference);

			State.getEmployees().add(employee);
			department.getEmployees().add(employee);
			role.getEmployees().add(employee);
		} catch (Exception e) {
			System.out.println("Failed to parse Employee line");
			e.printStackTrace();
		}
	}
}
