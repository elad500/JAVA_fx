package Model;

import Common.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Persistence {
	public static void saveData() throws IOException {
		Writer writer = new FileWriter("appdata.csv", false);

		for (Department department : State.getDepartments()) {
			writer.write(department.writeCsvRow());
		}

		for (Role role : State.getRoles()) {
			writer.write(role.writeCsvRow());
		}

		for (Employee employee : State.getEmployees()) {
			writer.write(employee.writeCsvRow());
		}

		writer.close();
	}

	public static void loadData() throws IOException {
		File file = new File("appdata.csv");
		if (!(file.exists())) {
			return;
		}

		Scanner reader = new Scanner(file);

		while (reader.hasNextLine()) {
			String line = reader.nextLine().trim();
			if ("".equals(line)) {
				continue;
			}
			String[] splitLine = line.split(Config.CSV_SEPARATOR);
			String type = splitLine[0];
			switch (type) {
			case "Department":
				Department.createFromCsvRow(splitLine);
				break;
			case "Role":
				Role.createFromCsvRow(splitLine);
				break;
			case "Employee":
				Employee.createFromCsvRow(splitLine);
				break;
			}
		}

		reader.close();
	}
}
