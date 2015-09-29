package dbop;

import java.sql.*;
import javax.swing.*;

public class MySqlLinker implements ILDBLinker {
	
	public MySqlLinker() {
		
	}
	
	@Override
	public Connection link(String url, String user, String password) {
		// TODO Auto-generated method stub
		
		try {
			// ������������һ��Ҳ��дΪ��Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
						
			// ����SQL����
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "�������ݿ����!\n" + e.toString(), "" +
					"���ݿ����", JOptionPane.ERROR_MESSAGE);
		}
		
		
		return null;
		
	}

	@Override
	public ResultSet getData(Connection conn, String statement) {
		// TODO Auto-generated method stub
		
		try {
			// ִ��SQL���
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(statement);
			
			return rs;
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "���ݲ�ѯ����!\n" + e.toString(), "" +
					"���ݿ����", JOptionPane.ERROR_MESSAGE);
		}
		
		
		return null;
	}

	@Override
	public boolean closeConnection(ResultSet rs, Connection conn) {
		// TODO Auto-generated method stub
		
		try {
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "�رմ���!\n" + e.toString(), "" +
					"���ݿ����", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
}