package Model;

import Common.Config;
import java.util.ArrayList;
import java.util.List;

public class Role implements CompanyProfit, CompanyEntity {
	private String name;
	private WorkPolicy workPolicy;
	private List<Employee> employees;

	public Role(String name, WorkPolicy workPolicy) {
		this.name = name;
		this.employees = new ArrayList<>();
		this.workPolicy = workPolicy;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public WorkPolicy getWorkPolicy() {
		return workPolicy;
	}

	@Override
	public List<Employee> getEmployees() {
		return employees;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public double calculateCompanyProfitInHours() {
		double sum = 0;
		for (Employee employee : employees) {
			sum += employee.calculateCompanyProfitInHours();
		}
		return sum;
	}

	@Override
	public double calculateCompanyProfitInNis() {
		double sum = 0;
		for (Employee employee : employees) {
			sum += employee.calculateCompanyProfitInNis();
		}
		return sum;
	}

	public String writeCsvRow() {
		StringBuilder builder = new StringBuilder();
		builder.append("Role").append(Config.CSV_SEPARATOR);
		builder.append(name).append(Config.CSV_SEPARATOR);
		builder.append(workPolicy.getAllowHourChange()).append(Config.CSV_SEPARATOR);
		builder.append(workPolicy.getRequireSync()).append(Config.CSV_SEPARATOR);
		builder.append(workPolicy.getCurrentPolicy().getPreferenceType()).append(Config.CSV_SEPARATOR);
		builder.append(workPolicy.getCurrentPolicy().getHourChange()).append(Config.CSV_NEW_LINE);
		return builder.toString();
	}

	public static void createFromCsvRow(String[] splitLine) {
		try {
			int hourChangeStr = Integer.parseInt(splitLine[5]);
			WorkPreference preference = new WorkPreference(WorkPreference.PreferenceType.valueOf(splitLine[4]),
					hourChangeStr);
			WorkPolicy workPolicy = new WorkPolicy(Boolean.parseBoolean(splitLine[2]),
					Boolean.parseBoolean(splitLine[3]), preference);
			Role role = new Role(splitLine[1], workPolicy);
			State.getRoles().add(role);
		} catch (Exception e) {
			System.out.println("Failed to parse Role line");
			e.printStackTrace();
		}
	}
}
