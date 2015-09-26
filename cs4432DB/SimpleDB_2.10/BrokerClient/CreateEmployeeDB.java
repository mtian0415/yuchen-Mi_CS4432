
import java.sql.*;

import simpledb.remote.SimpleDriver;



public class CreateEmployeeDB{
    public static void main(String[] args) {
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			String url = "jdbc:simpledb://localhost";
			//String url = "jdbc:derby://localhost/studentdb;create=true";
			conn = d.connect(url, null);
			Statement stmt = conn.createStatement();

			String s = "create table EMPLOYEE(EId int, EName varchar(10), dpt int, EnrolledYear int)";
			stmt.executeUpdate(s);
			System.out.println("Table EMPLOYEE created.");

			s = "insert into EMPLOYEE(EId, EName, dID, EnrolledYear) values ";
			String[] employeeVals = {"(1, 'joe', 80, 2014)",
									 "(2, 'amy', 1, 2014)",
									 "(3, 'max', 80, 2005)",
									 "(4, 'sue', 1, 2005)",
									 "(5, 'bob', 1, 2003)",
									 "(6, 'kim', 1, 2009)",
									 "(7, 'art', 2, 2008)",
									 "(8, 'pat', 1, 2009)",
									 "(9, 'lee', 80, 2003)",
									 "(10, 'max', 4, 2006)",
									 "(11, 'sue', 80, 2007)",
									 "(12, 'bob', 7, 2003)",
									 "(13, 'kim', 80, 2009)",
									 "(14, 'mi', 6, 2008)",
									 "(15, 'Jia', 80, 2009)",
									 "(16, 'yuchen', 80, 2003)",
									 "(17, 'zhoukai', 4, 2006)",
									 "(18, 'yanyan', 7, 2007)",
									 "(19, 'yifan', 80, 2003)",
									 "(20, 'qingyu', 7, 2009)",
									 "(21, 'elke', 6, 2008)",
									 "(22, 'erika', 6, 2009)",
									 "(23, 'tj', 80, 2003)" };	
			
			for (int i=0; i< employeeVals.length; i++)
				stmt.executeUpdate(s + employeeVals[i]);
			System.out.println("EMPLOYEE records inserted.");

			s = "create table SALARY(EnrollId int, sarlary(20), DeptId int)";
			stmt.executeUpdate(s);
			System.out.println("Table SALARY created.");

			s = "insert into SALARY(EnrollId, SALARY, DeptId) values ";
			String[] salaryvals = {"(12, '3000', 2)",
								   "(22, '5000', 2)",
								   "(32, '6000', 3)",
								   "(42, '6000', 3)",
								   "(52, '4000', 4)",
								   "(62, '4000', 4)"};
			for (int i=0; i< salaryvals.length; i++)
				stmt.executeUpdate(s + salaryvals[i]);
			System.out.println("SALARY records inserted.");	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
