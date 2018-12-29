package com.analyzer.html.vo;

import java.util.List;

/**
 * Clase para crear el objeto en que se parsea el json respuesta de la api que
 * lleva a cabo el análisis morfológico
 * 
 * @author Raúl
 *
 */
public class TokenList {

	private String type;
	private String form;
	private int id;
	private int inip;
	private int endp;
	private Style style;
	private String separation;
	private String quote_level;
	private String affected_by_negation;
	private List<SyntacticTree> syntactic_tree_relation_list;
	private List<Analysis> analysis_list;
	private List<TokenList> token_list;

	public List<SyntacticTree> getSyntactic_tree_relation_list() {
		return syntactic_tree_relation_list;
	}

	public void setSyntactic_tree_relation_list(List<SyntacticTree> syntactic_tree_relation_list) {
		this.syntactic_tree_relation_list = syntactic_tree_relation_list;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInip() {
		return inip;
	}

	public void setInip(int inip) {
		this.inip = inip;
	}

	public int getEndp() {
		return endp;
	}

	public void setEndp(int endp) {
		this.endp = endp;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public String getSeparation() {
		return separation;
	}

	public void setSeparation(String separation) {
		this.separation = separation;
	}

	public String getQuote_level() {
		return quote_level;
	}

	public void setQuote_level(String quote_level) {
		this.quote_level = quote_level;
	}

	public String getAffected_by_negation() {
		return affected_by_negation;
	}

	public void setAffected_by_negation(String affected_by_negation) {
		this.affected_by_negation = affected_by_negation;
	}

	public List<Analysis> getAnalysis_list() {
		return analysis_list;
	}

	public void setAnalysis_list(List<Analysis> analysis_list) {
		this.analysis_list = analysis_list;
	}

	public List<TokenList> getToken_list() {
		return token_list;
	}

	public void setToken_list(List<TokenList> token_list) {
		this.token_list = token_list;
	}

}
