package com.analyzer.html.vo;

import java.util.List;

public class Slide {

	private int numItalics;
	private int numBold;
	private int numUnderline;
	private int numUpperCase;
	private int numWords;
	private int numShadowWords;
	private List<String> colors;
	private List<String> backgroundColors;
	private List<String> sizeText;
	private List<String> fonts;
	private List<Double> numbers;
	private List<String> specialChars;
	private List<String> orderChars;
	private List<String> wrongDateFormat;
	private List<String> romanNumber;
	private List<Sentence> listSentences;
	private String content;

	public List<Sentence> getListSentences() {
		return listSentences;
	}

	public void setListSentences(List<Sentence> listSentences) {
		this.listSentences = listSentences;
	}

	public List<String> getRomanNumber() {
		return romanNumber;
	}

	public void setRomanNumber(List<String> romanNumber) {
		this.romanNumber = romanNumber;
	}

	public List<String> getOrderChars() {
		return orderChars;
	}

	public void setOrderChars(List<String> orderChars) {
		this.orderChars = orderChars;
	}

	public List<String> getSpecialChars() {
		return specialChars;
	}

	public void setSpecialChars(List<String> specialChars) {
		this.specialChars = specialChars;
	}

	public int getNumItalics() {
		return numItalics;
	}

	public void setNumItalics(int numItalics) {
		this.numItalics = numItalics;
	}

	public int getNumBold() {
		return numBold;
	}

	public void setNumBold(int numBold) {
		this.numBold = numBold;
	}

	public int getNumUnderline() {
		return numUnderline;
	}

	public void setNumUnderline(int numUnderline) {
		this.numUnderline = numUnderline;
	}

	public int getNumUpperCase() {
		return numUpperCase;
	}

	public void setNumUpperCase(int numUpperCase) {
		this.numUpperCase = numUpperCase;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNumWords() {
		return numWords;
	}

	public void setNumWords(int numWords) {
		this.numWords = numWords;
	}

	public int getNumShadowWords() {
		return numShadowWords;
	}

	public void setNumShadowWords(int numShadowWords) {
		this.numShadowWords = numShadowWords;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public List<String> getBackgroundColors() {
		return backgroundColors;
	}

	public void setBackgroundColors(List<String> backgroundColors) {
		this.backgroundColors = backgroundColors;
	}

	public List<String> getSizeText() {
		return sizeText;
	}

	public void setSizeText(List<String> sizeText) {
		this.sizeText = sizeText;
	}

	public List<String> getFonts() {
		return fonts;
	}

	public void setFonts(List<String> fonts) {
		this.fonts = fonts;
	}

	public List<Double> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Double> numbers) {
		this.numbers = numbers;
	}

	public List<String> getWrongDateFormat() {
		return wrongDateFormat;
	}

	public void setWrongDateFormat(List<String> wrongDateFormat) {
		this.wrongDateFormat = wrongDateFormat;
	}

}
