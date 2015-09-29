package dataop;

import dbop.*;
import java.sql.*;
import javax.swing.*;

import mystructre.WordStructure;


public class NormalWordMeaningSaving implements ILWordSaving {
	private String url = "jdbc:mysql://localhost/dict";
	private String user = "root";
	private String pwd = "xc123456";
	private Connection conn;
	private ResultSet rs;
	private String stmt;
	private ILDBLinker mysqlLinker = new MySqlLinker();
	private WordStructure[] word;
	private int index = 0;
	
	
	public NormalWordMeaningSaving() {
		
	}
	
	@Override
	public WordStructure[] query(String strWord) {
		// TODO Auto-generated method stub
		//... ��ѯstrWord�Ļ�������
		
		if (strWord.equalsIgnoreCase("")) {
			return null;
		}
		
		try {
			conn = mysqlLinker.link(url, user, pwd);		
			
			char firstLetterInWord = strWord.charAt(0);
			
			// ģ�����ң������ַ��ļ���
			stmt = "select * from wordsaving where word like " 
					+ "\'" + firstLetterInWord + "%" + "\' order by word";
			
			rs = mysqlLinker.getData(conn, stmt);
			// �α��Ƶ����һ��
			rs.last();
			
			// �õ��к�
			int row = rs.getRow();
			
			// �Ƶ���һ��֮ǰ
			rs.beforeFirst();
	
			if (row != 0) {
				word = new WordStructure[row];
			
				index = 0;
			}			
			
			while (rs.next()) {
				word[index] = new WordStructure();
				word[index].setWord(rs.getString("word"));
				word[index].setNormalMeaningOfWord(rs.getString("normalmeaning"));
				word[index].setMeaningFromNet(rs.getString("meaningfromnet"));
				index++;
			}
			
			mysqlLinker.closeConnection(rs, conn);
			
			return word;
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
					"Error in class \"NormalWordMeaningSaving\" in function \'query\' .\n"
		             + e.toString(), "" + "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
}