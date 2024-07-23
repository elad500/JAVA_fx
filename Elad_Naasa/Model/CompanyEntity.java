package Model;

import java.util.List;

public interface CompanyEntity {
//
	abstract String getName();

	abstract WorkPolicy getWorkPolicy();

	abstract List<Employee> getEmployees();

	void setName(String name);

}
