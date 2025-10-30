package org.employee;

import org.employee.utils.DBConnection;

import java.sql.*;

public class Main {
    public static void main(String[] args)  {

        Connection con = DBConnection.getConnection();

    }
}
