import java.sql.*;

import simpledb.remote.SimpleDriver;

public class CreateBrokerDB {

	public static void main(String[] args) {
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			String s = "create table BROKER(BrID int, BrName varchar(10), BrPrice int)";
			stmt.executeUpdate(s);
			System.out.println("Table BROKER created.");

			s = "insert into BROKER(BrId, BrName, BrPrice) values ";
			String[] brovals = { "(1, 'Thomas', 180)",
					"(2, 'Charles', 190)", "(3, 'Jack', 150)",
					"(4, 'Nora', 160)", 
					"(5, 'Mila', 170)", 
					"(6, 'kim', 200)",
					 "(7, 'art', 180)",
					 "(8, 'pat', 190)",
					 "(9, 'lee', 130)",
					 "(10, 'max', 160)",
					 "(11, 'sue', 155)",
					 "(12, 'bob', 180)",
					 "(13, 'kim', 180)",
					 "(14, 'mi', 185)",
					 "(15, 'Jia', 190)",
					 "(16, 'yuchen', 120)",
					 "(17, 'zhoukai', 140)",
					 "(18, 'yanyan', 150)",
					 "(19, 'yifan', 155)",
					 "(20, 'qingyu', 150)",
					 "(21, 'elke', 170)",
					 "(22, 'erika',140)",
					 "(23, 'tj', 125)"};	
			for (int i = 0; i < brovals.length; i++)
				stmt.executeUpdate(s + brovals[i]);
			System.out.println("BROKER records inserted.");

			s = "create table CLIENT(ClID int, ClName varchar(8), BrokerID int, ClNetWorth int)";
			stmt.executeUpdate(s);
			System.out.println("Table CLIENT created.");

			s = "insert into CLIENT(ClId, ClName, BrokerID, ClNetWorth) values ";
			String[] clivals = { "(10, 'Alexis', 1, 10000)", "(11, 'Kevin', 3, 50000)",
					"(12, 'Gavin', 2, 2000)", "(13, 'Bella', 4, 3000)", "(14, 'Jose', 5, 2500)", "(15, 'Lincoln', 3, 20000)"};
			for (int i = 0; i < clivals.length; i++)
				stmt.executeUpdate(s + clivals[i]);
			System.out.println("CLIENT records inserted.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
