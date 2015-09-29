package mainframe;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;

import dataop.*;

public class MainEntrance extends JFrame {
	private JTabbedPane optionTabbedPane;
	private String[] strMenuOption = {"�ʵ�", "�Զ�����", "���߷���"};    // �˵�ѡ��
	private WordPanel wordPanel = new WordPanel();
	private ImageIcon backImg = new ImageIcon("icon/back.jpg");
	private JLabel labelForImg = new JLabel(backImg);
	
	public MainEntrance() {
		optionTabbedPane = new JTabbedPane();
		optionTabbedPane.setTabPlacement(JTabbedPane.TOP);	
		optionTabbedPane.addTab(strMenuOption[0], new WordPanel());
		optionTabbedPane.addTab(strMenuOption[1], new AutoTranslatePanel());
		optionTabbedPane.addTab(strMenuOption[2], new OnlineTranslatePanel());
		optionTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});		

		add(optionTabbedPane);
	}
	
	public static void main(String[] args) {
		MainEntrance e = new MainEntrance();
		// ��ȡ��Ļ�ĳ���
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension dimension = kit.getScreenSize();
		int screenHeight = dimension.height;
		int screenWidth = dimension.width;
		
		e.setTitle("Dict");
		e.setIconImage(Toolkit.getDefaultToolkit().createImage("icon/logo.jpg"));
		e.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		e.setSize(600, 500);
		e.setResizable(false);
		// ��Ļ����λ����ʾ����
		e.setLocation(screenWidth/4, screenHeight/7);
		e.setVisible(true);
	}
}