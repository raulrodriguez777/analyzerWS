package com.analyzer.html.vo;

/**
 * Clase para crear el objeto en que se parsea el json respuesta de la api que
 * lleva a cabo el análisis morfológico
 * 
 * @author Raúl
 *
 */
public class Status {
	private int code;
	private String msg;
	private int credits;
	private String remaining_credits;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getRemaining_credits() {
		return remaining_credits;
	}

	public void setRemaining_credits(String remaining_credits) {
		this.remaining_credits = remaining_credits;
	}

}
