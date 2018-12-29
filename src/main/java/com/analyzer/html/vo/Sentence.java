package com.analyzer.html.vo;

public class Sentence {

	private String content;
	private int numChars;
	private int numWords;
	private int numPronoun;
	private boolean secondPerson;
	private boolean formActive;
	private boolean haveSubject;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNumChars() {
		return numChars;
	}

	public void setNumChars(int numChars) {
		this.numChars = numChars;
	}

	public int getNumWords() {
		return numWords;
	}

	public void setNumWords(int numWords) {
		this.numWords = numWords;
	}

	public int getNumPronoun() {
		return numPronoun;
	}

	public void setNumPronoun(int numPronoun) {
		this.numPronoun = numPronoun;
	}

	public boolean isSecondPerson() {
		return secondPerson;
	}

	public void setSecondPerson(boolean secondPerson) {
		this.secondPerson = secondPerson;
	}

	public boolean isFormActive() {
		return formActive;
	}

	public void setFormActive(boolean formActive) {
		this.formActive = formActive;
	}

	public boolean isHaveSubject() {
		return haveSubject;
	}

	public void setHaveSubject(boolean haveSubject) {
		this.haveSubject = haveSubject;
	}

	public boolean isCorrectStructure() {
		return correctStructure;
	}

	public void setCorrectStructure(boolean correctStructure) {
		this.correctStructure = correctStructure;
	}

	private boolean correctStructure;

}
