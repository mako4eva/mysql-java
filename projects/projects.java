package projects;

import java.sql.Connection;

import projects.dao.DbConnection;

public class projects {

	public static void main(String[] args) {
		Connection c = DbConnection.getConnection();
	}

}
