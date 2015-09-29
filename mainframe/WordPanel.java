package mainframe;

// ����֮����
// 1. ���еĳ�������������ָ���ģ�û�������ֵ

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
	private final int PANEL_WIDTH = 105;           // ������ܿ��
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
	
	// �ݴ�����ĵ��ʣ���Ϊpre next�Ĺ���ʵ��
	private ArrayList<WordStructure> bufferList = new ArrayList<WordStructure>();
	// �α꣬ȷ��ѡ���ĸ�����
	private int bufIndex = 0;
	// �洢��ѯ���ؽ��
	private Vector<WordStructure> queryResultSet = new Vector<WordStructure>();
	private ILWordSaving wordSaving = new NormalWordMeaningSaving();
	private String normalMeaning = "�������壺";
	private String netMeaning = "������壺";
	
	// ���ݲ�ѯ�������� WordStructure[]����
	public WordStructure[] word; 
	
	// ���˴洢���ʣ���ʼ��list��Ҳ����ͨ��listModel����ɣ��������ǲ������ӵ�������
	// ������һ��string����洢����wordֵ
	String[] strWordForList;
	
	
	// ��屳��ͼƬ
	private ImageIcon backImg = new ImageIcon("icon/back.jpg");
	private JLabel LabelForBackImg = new JLabel(backImg);
			
	public WordPanel() {		
		
		preWordButton = new JButton(preButtonText);
		preWordButton.setBorderPainted(false);
		preWordButton.setForeground(Color.WHITE);
		preWordButton.setBackground(Color.BLACK);
		// ȡ��button�����ֱ߿�
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
					//...����wordMeaningArea����
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
					// ... ����wordMeaningArea����
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
				
				// ... ���ҵ��ʣ���wordMeaningArea����ʾ
				if (textOfInputField != null) {
					
					// �жϲ�ѯ�ĵ����ϴ��Ƿ��Ѿ����
					int index = isWordInSet(textOfInputField);
					
					// ���word��û�У�ȥ��ѯ���ݿ�
					if (index == -1) {
						word = wordSaving.query(textOfInputField);
						
						if (word != null) {
							
							strWordForList = new String[word.length];
							
							for (int i=0; i<word.length; i++) {
								
								queryResultSet.add(word[i]);
								
								strWordForList[i] = new String();
								strWordForList[i] = word[i].getWord();
								
								// ����ڼ������ҵ���ѯ�ĵ��ʣ�����textArea����ʾ����
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
							
							// ���������û�в鵽����
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
		
		
		// ... list����Ϊ���
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
					
					// ���ڸ��¼�������������ݣ�ԭ����룺��һ�ε������ʱ���ڶ��ε��������ʱ
					// ��ʱҲ���� valuechange�����Ի��������
					// ����������Ƚ���ӵ�������bufferlist���һ�����ݣ����Ա����ظ���� --�в�ͨ
					
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
	
	/* �ж�strWord�Ƿ���word������ */
	private int isWordInSet(String strWord) {
			
		if (word == null) {
			return -1;
		}
		
		// ����ĸ��ͬ
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
	
	
	/* ����wordMeaningArea���� */
	private void setwordMeaningArea(String strWord, String strNormalMeaning, 
			String strNetMeaning) {
		String text = strWord + "\n" 
				+ normalMeaning + strNormalMeaning + "\n"
				+ netMeaning + strNetMeaning + "\n";
		wordMeaningArea.setText(text);
	}
	
}