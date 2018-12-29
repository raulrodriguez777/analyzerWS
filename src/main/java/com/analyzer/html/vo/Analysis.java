package com.analyzer.html.vo;

/**
 * Clase para crear el objeto en que se parsea el json respuesta de la api que
 * lleva a cabo el análisis morfológico
 * 
 * @author Raúl
 *
 */
public class Analysis {

	private String origin;
	private String tag;
	private String lemma;
	private String original_form;
	private String tag_info;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getOriginal_form() {
		return original_form;
	}

	public void setOriginal_form(String original_form) {
		this.original_form = original_form;
	}

	public String getTag_info() {
		return tag_info;
	}

	public void setTag_info(String tag_info) {
		this.tag_info = tag_info;
	}

}
