package org.employee;

import org.employee.dao.EmployeeDAO;
import org.employee.model.Employee;
import org.employee.utils.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * EmployeeCLI - simple menu-driven console app to perform CRUD on employees.
 */
public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final EmployeeDAO dao;

    public Main(EmployeeDAO dao) {
        this.dao = dao;
    }

    private void showMenu() {
        System.out.println("\n=== Employee Management ===");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Update Employee");
        System.out.println("4. Delete Employee (Hard Delete)");
        System.out.println("5. Soft Delete Employee (Terminate)");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private void run() {
        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addEmployeeFlow();
                case "2" -> viewAllEmployees();
                case "3" -> updateEmployeeFlow();
                case "4" -> deleteEmployeeFlow(false);
                case "5" -> deleteEmployeeFlow(true);
                case "6" -> {
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    // ---------- Flow Methods ----------

    private void addEmployeeFlow() {
        try {
            System.out.println("\n--- Add Employee ---");
            System.out.print("First name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Last name: ");
            String lastName = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Phone (optional): ");
            String phone = scanner.nextLine().trim();

            System.out.print("Department (Engineering/HR/Finance/Sales/Marketing/Operations): ");
            String department = scanner.nextLine().trim();

            System.out.print("Salary (e.g., 45000.00): ");
            double salary = parseDoubleInput(scanner.nextLine().trim(), 0.0);

            System.out.print("Hire date (YYYY-MM-DD) or blank for today: ");
            String dateStr = scanner.nextLine().trim();
            Date hireDate = (dateStr.isEmpty()) ? new Date(System.currentTimeMillis()) : Date.valueOf(dateStr);

            System.out.print("Status (Active/Probation/Terminated) [default Active]: ");
            String status = scanner.nextLine().trim();
            if (status.isEmpty()) status = "Active";

            Employee emp = new Employee();
            emp.setFirst_name(firstName);
            emp.setLast_name(lastName);
            emp.setEmail(email);
            emp.setPhone(phone.isEmpty() ? null : phone);
            emp.setDepartment(department);
            emp.setSalary(salary);
            emp.setJoining_date(hireDate);
            emp.setStatus(status);

            dao.addEmployee(emp);
        } catch (IllegalArgumentException iae) {
            System.out.println("Input error: " + iae.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to add employee: " + e.getMessage());
        }
    }

    private void viewAllEmployees() {
        try {
            List<Employee> list = dao.getAllEmployees();
            if (list.isEmpty()) {
                System.out.println("\nNo employees found.");
                return;
            }
            System.out.println("\n--- Employees ---");
            System.out.printf("%-4s %-15s %-15s %-25s %-12s %-10s %-12s %-10s%n",
                    "ID", "First Name", "Last Name", "Email", "Dept", "Salary", "Hire Date", "Status");
            for (Employee e : list) {
                String hireDate = (e.getJoining_date() == null) ? "-" : e.getJoining_date().toString();
                System.out.printf("%-4d %-15s %-15s %-25s %-12s %-10.2f %-12s %-10s%n",
                        e.getId(),
                        nullSafe(e.getFirst_name()),
                        nullSafe(e.getLast_name()),
                        nullSafe(e.getEmail()),
                        nullSafe(e.getDepartment()),
                        e.getSalary(),
                        hireDate,
                        nullSafe(e.getStatus())
                );
            }
        } catch (Exception ex) {
            System.out.println("Error fetching employees: " + ex.getMessage());
        }
    }

    private void updateEmployeeFlow() {
        try {
            System.out.print("\nEnter employee ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            // fetch current record
            Employee current = dao.getEmployeeById(id);
            if (current == null) {
                System.out.println("No employee found with ID: " + id);
                return;
            }

            System.out.println("Leave a field blank to keep the current value.");
            System.out.printf("First name [%s]: ", current.getFirst_name());
            String firstName = scanner.nextLine().trim();
            if (!firstName.isEmpty()) current.setFirst_name(firstName);

            System.out.printf("Last name [%s]: ", current.getLast_name());
            String lastName = scanner.nextLine().trim();
            if (!lastName.isEmpty()) current.setLast_name(lastName);

            System.out.printf("Email [%s]: ", current.getEmail());
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) current.setEmail(email);

            System.out.printf("Phone [%s]: ", nullSafe(current.getPhone()));
            String phone = scanner.nextLine().trim();
            if (!phone.isEmpty()) current.setPhone(phone);

            System.out.printf("Department [%s]: ", nullSafe(current.getDepartment()));
            String dept = scanner.nextLine().trim();
            if (!dept.isEmpty()) current.setDepartment(dept);

            System.out.printf("Salary [%.2f]: ", current.getSalary());
            String salaryStr = scanner.nextLine().trim();
            if (!salaryStr.isEmpty()) current.setSalary(parseDoubleInput(salaryStr, current.getSalary()));

            System.out.printf("Hire date [%s] (YYYY-MM-DD): ", current.getJoining_date() == null ? "-" : current.getJoining_date().toString());
            String hd = scanner.nextLine().trim();
            if (!hd.isEmpty()) current.setJoining_date(Date.valueOf(hd));

            System.out.printf("Status [%s]: ", nullSafe(current.getStatus()));
            String status = scanner.nextLine().trim();
            if (!status.isEmpty()) current.setStatus(status);

            boolean ok = dao.updateEmployee(current);
            if (ok) System.out.println("Employee updated successfully.");
            else System.out.println("Update failed or no changes made.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Update error: " + e.getMessage());
        }
    }

    private void deleteEmployeeFlow(boolean soft) {
        try {
            System.out.print("\nEnter employee ID to " + (soft ? "soft-delete (terminate)" : "delete") + ": ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Are you sure? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("Operation cancelled.");
                return;
            }

            boolean result = soft ? dao.softDeleteEmployee(id) : dao.deleteEmployee(id);
            if (result) System.out.println((soft ? "Soft-deleted (terminated)" : "Deleted") + " employee with ID " + id);
            else System.out.println("No employee found with ID " + id);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Delete error: " + e.getMessage());
        }
    }

    // ---------- Helpers ----------

    private static String nullSafe(String s) {
        return s == null ? "-" : s;
    }

    private static double parseDoubleInput(String input, double fallback) {
        if (input == null || input.isEmpty()) return fallback;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format, using fallback: " + fallback);
            return fallback;
        }
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Unable to get DB connection. Exiting.");
                return;
            }

            EmployeeDAO dao = new EmployeeDAO(conn);
            Main cli = new Main(dao);
            cli.run();
        } catch (SQLException e) {
            System.out.println("Error while establishing DB connection: " + e.getMessage());
        }
    }
}
