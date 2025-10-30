package org.employee.dao;


import org.employee.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

        private Connection conn;

        public EmployeeDAO(Connection conn) {
            this.conn = conn;
        }

        public void addEmployee(Employee emp) {
            String sql = "INSERT INTO employee (first_name, last_name, email, phone, department, salary, joining_date, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, emp.getFirst_name());
                ps.setString(2, emp.getLast_name());
                ps.setString(3, emp.getEmail());
                ps.setString(4, emp.getPhone());
                ps.setString(5, emp.getDepartment());
                ps.setDouble(6, emp.getSalary());
                ps.setDate(7, emp.getJoining_date());
                ps.setString(8, emp.getStatus());

                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("✅ Employee added successfully!");
                }
            } catch (SQLException e) {
                System.out.println("❌ Error adding employee: " + e.getMessage());
            }
        }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setFirst_name(rs.getString("first_name"));
                emp.setLast_name(rs.getString("last_name"));
                emp.setEmail(rs.getString("email"));
                emp.setPhone(rs.getString("phone"));
                emp.setDepartment(rs.getString("department"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setJoining_date(rs.getDate("hire_date"));
                emp.setStatus(rs.getString("status"));
                emp.setCreated_at(rs.getTimestamp("created_at"));
                emp.setUpdated_at(rs.getTimestamp("updated_at"));

                employees.add(emp);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching employees: " + e.getMessage());
        }

        return employees;
    }


    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, phone=?, department=?, " +
                "salary=?, hire_date=?, status=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getFirst_name());
            ps.setString(2, emp.getLast_name());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getPhone());
            ps.setString(5, emp.getDepartment());
            ps.setDouble(6, emp.getSalary());
            ps.setDate(7, emp.getJoining_date());
            ps.setString(8, emp.getStatus());
            ps.setInt(9, emp.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Employee updated successfully!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error updating employee: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
