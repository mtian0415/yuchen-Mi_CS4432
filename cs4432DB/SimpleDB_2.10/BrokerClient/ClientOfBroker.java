import java.sql.*;
import simpledb.remote.SimpleDriver;

public class ClientOfBroker {
    public static void main(String[] args) {
		Connection conn = null;
		try {
			// Step 1: connect to database server
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);

			// Step 2: execute the query
			Statement stmt = conn.createStatement();
			String qry = "select BrName, ClName "
			           + "from BROKER, CLIENT "
			           + "where BrId = BrokerId";
			ResultSet rs = stmt.executeQuery(qry);

			// Step 3: loop through the result set
			System.out.println("Broker Name\tClient Name");
			while (rs.next()) {
				String BrName = rs.getString("BrName");
				String ClName = rs.getString("ClName");
				System.out.println(BrName + "\t\t" + ClName);
			}
			rs.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			// Step 4: close the connection
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
