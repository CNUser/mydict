package dbop;

import java.sql.*;

import javax.swing.*;

public class SqlServerLinker implements ILDBLinker {
	
	public SqlServerLinker() {
		
	}

	@Override
	public Connection link(String url, String user, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getData(Connection conn, String statement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean closeConnection(ResultSet rs, Connection conn) {
		// TODO Auto-generated method stub
		return false;
	}
}