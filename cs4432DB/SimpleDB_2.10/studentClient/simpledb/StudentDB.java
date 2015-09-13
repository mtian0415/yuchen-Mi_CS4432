import java.sql.*;
import simpledb.remote.SimpleDriver;

public class StudentDB {
	    public static void main(String[] args) {
			Connection conn = null;
			try {
				Driver d = new SimpleDriver();
				conn = d.connect("jdbc:simpledb://localhost", null);
				Statement stmt = conn.createStatement();

				String s = "create table STUDENT(SId int, SName varchar(10), MajorId int, GradYear int)";
				stmt.executeUpdate(s);
				System.out.println("Table STUDENT created.");

				s = "insert into STUDENT(SId, SName, MajorId, GradYear) values ";
				String[] studvals = {"(1, 'Mi', 100, 2016)",
									 "(2, 'Jia', 20, 2015)",
									 "(3, 'Jaden', 10, 2005)",
									 "(4, 'Maxwell', 20, 2008)",
									 "(5, 'Elke', 30, 2009)",
									 "(6, 'Erika', 20, 2009)",
									 "(7, 'Chris', 30, 2009)",
									 "(8, 'pat', 20, 2002)",
									 "(9, 'lee', 10, 2005)"};
				for (int i=0; i<studvals.length; i++)
					stmt.executeUpdate(s + studvals[i]);
				System.out.println("STUDENT records inserted.");

				s = "create table DEPT(DId int, DName varchar(8))";
				stmt.executeUpdate(s);
				System.out.println("Table DEPT created.");

				s = "insert into DEPT(DId, DName) values ";
				String[] deptvals = {"(10, 'compsci')",
									 "(20, 'math')",
									 "(30, 'drama')"};
				for (int i=0; i<deptvals.length; i++)
					stmt.executeUpdate(s + deptvals[i]);
				System.out.println("DEPT records inserted.");
	
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
