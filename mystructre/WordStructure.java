package mystructre;

public class WordStructure {
	private String word;
	private String normalMeaningOfWord;
	private String meaningFromNet;
	
	public WordStructure() {
		word = null;
		normalMeaningOfWord = null;
		meaningFromNet = null;
	}
	
	public WordStructure(String word, String normalMeaningOfWord,
			String meaningFromNet) {
		super();
		this.word = word;
		this.normalMeaningOfWord = normalMeaningOfWord;
		this.meaningFromNet = meaningFromNet;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getNormalMeaningOfWord() {
		return normalMeaningOfWord;
	}

	public void setNormalMeaningOfWord(String normalMeaningOfWord) {
		this.normalMeaningOfWord = normalMeaningOfWord;
	}

	public String getMeaningFromNet() {
		return meaningFromNet;
	}

	public void setMeaningFromNet(String meaningFromNet) {
		this.meaningFromNet = meaningFromNet;
	}
	
}