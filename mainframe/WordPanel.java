package mainframe;

// 不足之处：
// 1. 所有的长宽，行列数都是指定的，没有用相对值

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dataop.*;
import mystructre.*;


public class WordPanel extends JPanel {
	private JButton preWordButton;
	private JButton nextWordButton;
	private String preButtonText = "<";
	private String nextButtonText = ">";
	
	private JTextField wordInputField;
	private int inputFieldInitialCols = 30;
	
	private JButton searchButton;
	private ImageIcon searchButtonIcon = new ImageIcon("icon/search.gif");
	private String searchButtonText = "Search";
	
	private JPanel PreNextButtonPanel;
	private JPanel ButtonFieldPanel;	
	
	private final int PANEL_HEIGHT = 15;
	private final int PNBUTTON_PANEL_WIDTH = 35;
	private final int PANEL_WIDTH = 105;           // 界面的总宽度
	private final int INPUTAREA_WIDTH = 40;
	private final int INTERVAL_BUTTONPANEL_TEXTAREA = 2;
	private final int INTERVAL_TEXTAREA_SEARCHBUTTON = 3;
	
	private JPanel listAreaPanel;
	private JScrollPane listScrollPane;
	private JList queryList;
	private int listHeight = 360;
	private int listWidth = 150;
	
	private JTextArea wordMeaningArea;
	private int wordMeaningAreaRows = 16;
	private int wordMeaningAreaCols = 30;
	
	private final int LIST_AREA_HEIGHT = 400;
	private final int LIST_WIDTH = 0;
	private final int AREA_WIDTH = 0;
	
	// 暂存输入的单词，作为pre next的功能实现
	private ArrayList<WordStructure> bufferList = new ArrayList<WordStructure>();
	// 游标，确定选择哪个单词
	private int bufIndex = 0;
	// 存储查询返回结果
	private Vector<WordStructure> queryResultSet = new Vector<WordStructure>();
	private ILWordSaving wordSaving = new NormalWordMeaningSaving();
	private String normalMeaning = "基本词义：";
	private String netMeaning = "网络词义：";
	
	// 数据查询函数返回 WordStructure[]类型
	public WordStructure[] word; 
	
	// 用了存储单词，初始化list，也可以通过listModel来完成，但是这是不会增加单个数据
	// 所以用一个string数组存储所有word值
	String[] strWordForList;
	
	
	// 面板背景图片
	private ImageIcon backImg = new ImageIcon("icon/back.jpg");
	private JLabel LabelForBackImg = new JLabel(backImg);
			
	public WordPanel() {		
		
		preWordButton = new JButton(preButtonText);
		preWordButton.setBorderPainted(false);
		preWordButton.setForeground(Color.WHITE);
		preWordButton.setBackground(Color.BLACK);
		// 取消button内文字边框
		preWordButton.setFocusPainted(false);
		preWordButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				bufIndex--;
				
				if (bufIndex < 0) {
					bufIndex = 0;
				}
				
				else {				
					//...设置wordMeaningArea内容
					setwordMeaningArea( (bufferList.get(bufIndex)).getWord(), 
							(bufferList.get(bufIndex)).getNormalMeaningOfWord(), 
							(bufferList.get(bufIndex)).getMeaningFromNet() );
				}
				
				for (int i = 0; i < bufferList.size(); i++) {
					System.out.println( (bufferList.get(i)).getWord() +
							(bufferList.get(i)).getNormalMeaningOfWord() +
							(bufferList.get(i)).getMeaningFromNet() );
				}
			}			
		});
		
		nextWordButton = new JButton(nextButtonText);
		nextWordButton.setForeground(Color.WHITE);
		nextWordButton.setBackground(Color.BLACK);
		nextWordButton.setFocusPainted(false);
		nextWordButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				bufIndex++;
				
				if (bufIndex >= bufferList.size()) {
					bufIndex = bufferList.size() - 1;
				}
				else {
					// ... 设置wordMeaningArea内容
					setwordMeaningArea( (bufferList.get(bufIndex)).getWord(), 
							(bufferList.get(bufIndex)).getNormalMeaningOfWord(), 
							(bufferList.get(bufIndex)).getMeaningFromNet() );
				}
			}
			
		});
		
		PreNextButtonPanel = new JPanel();
//		PreNextButtonPanel.setBackground(Color.BLACK);
		PreNextButtonPanel.setLayout(new FlowLayout());
		PreNextButtonPanel.add(preWordButton);
		PreNextButtonPanel.add(nextWordButton);
		
		wordInputField = new JTextField(inputFieldInitialCols);
		wordInputField.setBorder(new EtchedBorder(Color.GRAY, Color.BLUE));
		
//		searchButton = new JButton(searchButtonText, searchButtonIcon);
		searchButton = new JButton(searchButtonIcon);
		searchButton.setBackground(Color.BLACK);
		searchButton.setFocusPainted(false);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String textOfInputField = wordInputField.getText();
				boolean hasWordTag = false;
				
				// ... 查找单词，在wordMeaningArea中显示
				if (textOfInputField != null) {
					
					// 判断查询的单词上次是否已经查过
					int index = isWordInSet(textOfInputField);
					
					// 如果word中没有，去查询数据库
					if (index == -1) {
						word = wordSaving.query(textOfInputField);
						
						if (word != null) {
							
							strWordForList = new String[word.length];
							
							for (int i=0; i<word.length; i++) {
								
								queryResultSet.add(word[i]);
								
								strWordForList[i] = new String();
								strWordForList[i] = word[i].getWord();
								
								// 如果在集合里找到查询的单词，则在textArea中显示词义
								if ( textOfInputField.equalsIgnoreCase(word[i].getWord()) ) {
									setwordMeaningArea(textOfInputField, 
											           word[i].getNormalMeaningOfWord(), 
											           word[i].getMeaningFromNet());
									
									bufferList.add(word[i]);
									bufIndex++;
									
									hasWordTag = true;
								}
							} // end of for
							
							queryList.setListData(strWordForList);
							
							// 如果集合里没有查到单词
							if (hasWordTag == false) {
								wordMeaningArea.setText("Sorry, we have not included this word " + textOfInputField);
							}
							
							hasWordTag = false;
							
						} // end of if (word != null)
						else {
							wordMeaningArea.setText("Sorry, we have not included this word " + textOfInputField);
						}
						
					} // if (index == -1)
					else {
						String text = textOfInputField + "\n" 
								+ normalMeaning + word[index].getNormalMeaningOfWord() + "\n"
								+ netMeaning + word[index].getMeaningFromNet() + "\n";
						wordMeaningArea.setText(text);
					}
					
				} // end of if (textOfInputField != null)							
				
			} // end of actionPerformed
			
		});
		
		ButtonFieldPanel = new JPanel();
//		ButtonFieldPanel.setBackground(Color.black);
		ButtonFieldPanel.setLayout(new FlowLayout());
		ButtonFieldPanel.add(PreNextButtonPanel);
		ButtonFieldPanel.add(wordInputField);
		ButtonFieldPanel.add(searchButton);		
		
		
		// ... list内容为添加
		queryList = new JList();
		queryList.setBorder(new EtchedBorder(Color.GRAY, Color.BLUE));
		queryList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				
				if (queryList.getSelectedIndex() != -1) {
					setwordMeaningArea( word[queryList.getSelectedIndex()].getWord(), 
							word[queryList.getSelectedIndex()].getNormalMeaningOfWord(), 
							word[queryList.getSelectedIndex()].getMeaningFromNet() );
					
					// 由于改事件会两次添加数据，原因猜想：第一次点击本项时，第二次点击其他项时
					// 此时也属于 valuechange，所以会添加两次
					// 解决方法：比较添加的数据与bufferlist最后一项数据，可以避免重复添加 --行不通
					
					bufferList.add(word[queryList.getSelectedIndex()]);
					bufIndex++;
					
//					String wordInBufferList = (bufferList.get(bufferList.size() - 1)).getWord();
//					String wordInCliked = word[queryList.getSelectedIndex()].getWord();
//											
//						
//					if (wordInBufferList.equalsIgnoreCase(wordInCliked)) {
//						bufferList.add(word[queryList.getSelectedIndex()]);
//						bufIndex++;
//					}
				}	
				
			}
		});
		
		listScrollPane = new JScrollPane(queryList);
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setPreferredSize(new Dimension(listWidth, listHeight));
		
		wordMeaningArea = new JTextArea(wordMeaningAreaRows, wordMeaningAreaCols);
		wordMeaningArea.setEditable(false);		
		wordMeaningArea.setBorder(new EtchedBorder(Color.GRAY, Color.BLUE));
		wordMeaningArea.setFont(new Font(Font.DIALOG, 1, 15));
		
		listAreaPanel = new JPanel();
//		listAreaPanel.setLayout(new BorderLayout());
//		listAreaPanel.add(listScrollPane, BorderLayout.WEST);
//		listAreaPanel.add(wordMeaningArea, BorderLayout.CENTER);
		listAreaPanel.setLayout(new FlowLayout());
		listAreaPanel.add(listScrollPane);
		listAreaPanel.add(wordMeaningArea);
		
//		setLayout(new BorderLayout());
//		add(ButtonFieldPanel, BorderLayout.NORTH);
//		add(listAreaPanel, BorderLayout.CENTER);
		
		setLayout(new FlowLayout());
		add(ButtonFieldPanel);
		add(listAreaPanel);
		
		
		
	} // end of NormalWordMeaning constructor
	
	/* 判断strWord是否在word数组中 */
	private int isWordInSet(String strWord) {
			
		if (word == null) {
			return -1;
		}
		
		// 首字母不同
		if ( strWord.charAt(0) != (word[0].getWord()).charAt(0) ) {
			return -1;
		}
		
		for (int i = 0; i < word.length; i++) {
			if (strWord.equalsIgnoreCase(word[i].getWord())) {
				return i;
			}
		}
		
		return -1;
		
	} // end of isWordInSetTail
	
	
	/* 设置wordMeaningArea内容 */
	private void setwordMeaningArea(String strWord, String strNormalMeaning, 
			String strNetMeaning) {
		String text = strWord + "\n" 
				+ normalMeaning + strNormalMeaning + "\n"
				+ netMeaning + strNetMeaning + "\n";
		wordMeaningArea.setText(text);
	}
	
}