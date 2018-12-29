package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.provider.PropertyValuesProvider;
import com.analyzer.html.vo.Rule;

/**
 * Clase padre de todas las reglas, donde se incluyen métodos comunes entre
 * estas
 * 
 * @author Raúl
 *
 */
public class CommonRules {

	private static Logger logger = Logger.getLogger(CommonRules.class);

	/**
	 * Carga del fichero properties
	 * 
	 * @return fichero properties cargado
	 * @throws IOException
	 * 
	 */
	protected Properties loadProperties(String file) throws IOException {

		logger.info("Cargando fichero: " + file);

		// Cargamos el fichero properties
		PropertyValuesProvider properties = new PropertyValuesProvider();
		return properties.getPropertiesValues(file);

	}

	/**
	 * Comprueba si la definición de la regla en el fichero properties es correcta
	 * 
	 * @param definitionRule La regla a buscar en el properties
	 * @return La regla si OK o null si hay algún error
	 * @throws IOException
	 */
	protected Rule allOK(String definitionRule) throws IOException {

		logger.info("Analizando configuración de la regla en el properties");

		// Carga del fichero properties
		Properties pRules = loadProperties("rules-definition.properties");

		if (StringUtils.isNotBlank(pRules.getProperty(definitionRule))
				&& pRules.getProperty(definitionRule).split("-").length == 2
				&& pRules.getProperty(definitionRule).split("-")[1].equalsIgnoreCase("ACTIVE")) {

			int ruleId = Integer.parseInt(pRules.getProperty(definitionRule).split("-")[0]);

			Rule rule = new Rule();
			rule.setId(Integer.parseInt(pRules.getProperty("rules.id" + ruleId)));
			rule.setName(pRules.getProperty("rules.name" + ruleId));
			rule.setDescription(pRules.getProperty("rules.description" + ruleId));
			rule.setPass(true);
			rule.setReason("Not Apply");

			return rule;

		} else {
			return null;
		}

	}

}
