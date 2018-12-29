package com.analyzer.html.vo;

/**
 * Clase para crear el objeto en que se parsea el json respuesta de la api que
 * lleva a cabo el análisis morfológico
 * 
 * @author Raúl
 *
 */
public class Style {

	private String isBold;
	private String isItalics;
	private String isUnderlined;
	private String isTitle;

	public String getIsBold() {
		return isBold;
	}

	public void setIsBold(String isBold) {
		this.isBold = isBold;
	}

	public String getIsItalics() {
		return isItalics;
	}

	public void setIsItalics(String isItalics) {
		this.isItalics = isItalics;
	}

	public String getIsUnderlined() {
		return isUnderlined;
	}

	public void setIsUnderlined(String isUnderlined) {
		this.isUnderlined = isUnderlined;
	}

	public String getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(String isTitle) {
		this.isTitle = isTitle;
	}

}
