package com.analyzer.html.vo;

import java.util.List;

/**
 * Clase para crear el objeto en que se parsea el json respuesta de la api que
 * lleva a cabo el análisis morfológico
 * 
 * @author Raúl
 *
 */
public class Data {

	private Status status;
	private List<TokenList> token_list;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<TokenList> getToken_list() {
		return token_list;
	}

	public void setToken_list(List<TokenList> token_list) {
		this.token_list = token_list;
	}

}
