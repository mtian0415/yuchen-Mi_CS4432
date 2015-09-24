import java.sql.*;
import simpledb.remote.SimpleDriver;

public class ChangeBrPrice {
    public static void main(String[] args) {
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			String cmd = "update BrokerT set BrPrice=165 "
			           + "where SName = 'Mila'";
			stmt.executeUpdate(cmd);
			System.out.println("The Broker price for Mila is now 165.");
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
