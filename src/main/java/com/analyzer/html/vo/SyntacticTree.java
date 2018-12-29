package com.analyzer.html.vo;

/**
 * Clase para crear el objeto en que se parsea el json respuesta de la api que
 * lleva a cabo el análisis morfológico
 * 
 * @author Raúl
 *
 */
public class SyntacticTree {

	private int id;
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
