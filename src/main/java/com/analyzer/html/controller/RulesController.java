package com.analyzer.html.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.analyzer.html.provider.PropertyValuesProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.analyzer.html.vo.Rule;

/**
 * Controlador para la consulta de las reglas definidas
 * 
 * @author Raúl
 *
 */
@Controller
public class RulesController {

	private static Logger logger = Logger.getLogger(RulesController.class);
	private static final String RULEID = "rules.id";
	private static final String RULENAME = "rules.name";
	private static final String RULEDESCRIPTION = "rules.description";
	private static final String NOTDEFINED = "Not Defined";
	private static final String KEYTYPE = "Content-Type";
	private static final String VALUETYPE = "application/json;charset=UTF-8";

	/**
	 * Carga del fichero properties
	 * 
	 * @return fichero properties cargado
	 * @throws IOException
	 * 
	 */
	private Properties loadProperties(String file) throws IOException {

		logger.info("Cargando fichero: " + file);

		// Cargamos el fichero properties
		PropertyValuesProvider properties = new PropertyValuesProvider();
		return properties.getPropertiesValues(file);

	}

	/**
	 * Consulta todas las reglas definidas
	 * 
	 * @return: Lista con todas las reglas implementadas
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/api/rules")
	@ResponseBody
	public ResponseEntity<String> getAllrules() throws IOException {

		logger.info("Consulta todas las reglas definidas");

		// Cargamos los ficheros properties
		Properties pMess = loadProperties("analyzer.properties");
		Properties pRules = loadProperties("rules-definition.properties");

		try {

			List<Rule> listRules = new ArrayList<>();

			boolean hayRegla = true;
			int i = 1;

			while (hayRegla) {

				if (StringUtils.isNotBlank(pRules.getProperty(RULEID + i))) {

					Rule r = new Rule();
					r.setId(Integer.parseInt(pRules.getProperty(RULEID + i)));
					if (StringUtils.isNotBlank(pRules.getProperty(RULENAME + i))) {

						r.setName(pRules.getProperty(RULENAME + i));

					} else {
						r.setName(NOTDEFINED);
					}
					if (StringUtils.isNotBlank(pRules.getProperty(RULEDESCRIPTION + i))) {

						r.setDescription(pRules.getProperty(RULEDESCRIPTION + i));

					} else {
						r.setDescription(NOTDEFINED);
					}
					// Añadimos la regla a la lista
					listRules.add(r);

					i++;

				} else {
					hayRegla = false;
				}

			}

			if (listRules.isEmpty()) {
				return new ResponseEntity<>(pMess.getProperty("cmu-errors.notRules"), HttpStatus.OK);

			} else {

				// Serializamos la lista de reglas
				Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
				String ruleJson = prettyGson.toJson(listRules);

				HttpHeaders headers = new HttpHeaders();
				headers.add(KEYTYPE, VALUETYPE);

				return new ResponseEntity<>(ruleJson, headers, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Se ha producido un error en la consulta de las reglas");
			return new ResponseEntity<>(pMess.getProperty("cmu-errors.getRulesError"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Consulta la regla concreta que se pasa como parámetro
	 * 
	 * @return Devuelve la regla consultada
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/api/rules/{id_rule}")
	@ResponseBody
	public ResponseEntity<String> getrule(@PathVariable("id_rule") Integer ruleId) throws IOException {

		logger.info("Consulta todas la regla pasada por parámetro");

		// Cargamos el fichero properties donde se definien las reglas
		Properties pMess = loadProperties("analyzer.properties");
		Properties pRules = loadProperties("rules-definition.properties");

		try {

			if (StringUtils.isNotBlank(pRules.getProperty(RULEID + ruleId))) {

				Rule rule = new Rule();

				rule.setId(Integer.parseInt(pRules.getProperty(RULEID + ruleId)));
				if (StringUtils.isNotBlank(pRules.getProperty(RULENAME + ruleId))) {

					rule.setName(pRules.getProperty(RULENAME + ruleId));

				} else {
					rule.setName(NOTDEFINED);
				}
				if (StringUtils.isNotBlank(pRules.getProperty(RULEDESCRIPTION + ruleId))) {

					rule.setDescription(pRules.getProperty(RULEDESCRIPTION + ruleId));

				} else {
					rule.setDescription(NOTDEFINED);
				}

				// Serializamos la regla
				Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
				String ruleJson = prettyGson.toJson(rule);

				HttpHeaders headers = new HttpHeaders();
				headers.add(KEYTYPE, VALUETYPE);

				logger.info("Se ha llevado a cabo la consulta de la regla correctamente");
				return new ResponseEntity<>(ruleJson, headers, HttpStatus.OK);

			} else {

				logger.error("No se ha encontrado la regla pasada por parámetro");
				return new ResponseEntity<>(pMess.getProperty("cmu-errors.notFoundRule"), HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {

			logger.error("Se ha producido un error en la consulta de la regla");
			return new ResponseEntity<>(pMess.getProperty("cmu-errors.getRulesError"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
