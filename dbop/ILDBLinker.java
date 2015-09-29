package dbop;

import java.sql.*;

public interface ILDBLinker {
	public Connection link(String url, String user, String password);
	public ResultSet getData(Connection conn, String statement);
	public boolean closeConnection(ResultSet rs, Connection conn);
}