import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import simpledb.remote.SimpleDriver;

public class ChangeErrolledYear {
    public static void main(String[] args) {
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("update EMPLOYEE set EnrolledYear=2015 where dID='80'");
			System.out.println("Joe's EnrolledYear change to 2015.");
			System.out.println("Max's EnrolledYear change to 2015.");
			System.out.println("Lee's EnrolledYear change to 2015.");
			System.out.println("Sue's EnrolledYear change to 2015.");
			System.out.println("Kim's EnrolledYear change to 2015.");
			System.out.println("Jia's EnrolledYear change to 2015.");
			System.out.println("YuChen's EnrolledYear change to 2015.");
			System.out.println("YiFan's EnrolledYear change to 2015.");
			System.out.println("TJ's EnrolledYear change to 2015.");
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
