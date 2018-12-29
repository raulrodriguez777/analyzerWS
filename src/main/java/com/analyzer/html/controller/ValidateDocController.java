package com.analyzer.html.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.analyzer.html.provider.AnalyzerHtmlProvider;
import com.analyzer.html.provider.CreateConnectionProvider;
import com.analyzer.html.provider.PropertyValuesProvider;
import com.analyzer.html.rules.RuleBolds;
import com.analyzer.html.rules.RuleColorBackground;
import com.analyzer.html.rules.RuleColorText;
import com.analyzer.html.rules.RuleCorrectStructure;
import com.analyzer.html.rules.RuleDateFormat;
import com.analyzer.html.rules.RuleFontText;
import com.analyzer.html.rules.RuleFormPasive;
import com.analyzer.html.rules.RuleHasSubject;
import com.analyzer.html.rules.RuleItalics;
import com.analyzer.html.rules.RuleMaxNumber;
import com.analyzer.html.rules.RuleMaxPronoun;
import com.analyzer.html.rules.RuleMaxWords;
import com.analyzer.html.rules.RuleOrderChars;
import com.analyzer.html.rules.RuleRomanNumber;
import com.analyzer.html.rules.RuleSecondPerson;
import com.analyzer.html.rules.RuleSentenceMaxChars;
import com.analyzer.html.rules.RuleSentenceMaxWords;
import com.analyzer.html.rules.RuleShadowText;
import com.analyzer.html.rules.RuleSizeText;
import com.analyzer.html.rules.RuleSpecialChars;
import com.analyzer.html.rules.RuleUnderLine;
import com.analyzer.html.rules.RuleUpperCase;
import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * Controlador para validar las reglas implementadas
 * 
 * @author Raúl
 *
 */
@Controller
public class ValidateDocController {

	private static Logger logger = Logger.getLogger(ValidateDocController.class);

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

	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/api/documents/{id_document}/checkConformance")
	@ResponseBody
	public ResponseEntity<String> validateAllRules(@PathVariable("id_document") String documentId) throws IOException {

		logger.info("Consulta todas las reglas...");

		List<Rule> listRules = new ArrayList<>();

		// Cargamos los ficheros properties
		Properties pMess = loadProperties("analyzer.properties");

		try {

			// Nos conectamos a la bbdd y utilizamos la colección (definidas en
			// analyzer.properties)
			DBCollection collection = CreateConnectionProvider.createConnecion();

			if (collection != null) {

				ObjectId docID = new ObjectId(documentId);
				BasicDBObject d = new BasicDBObject();
				d.put("_id", docID);

				if (collection.getCount(d) == 1) {

					logger.info("Se ha encontrado el documento");

					if (collection.find(d).one().containsField("content")
							&& StringUtils.isNotBlank(collection.find(d).one().get("content").toString())) {

						// Recuperamos las caracteristicas de la diapositiva
						Slide featureSlide = AnalyzerHtmlProvider
								.analyzer(collection.find(d).one().get("content").toString());

						// REGLA 1:
						RuleFontText rFontText = new RuleFontText();
						Rule ruleFontText = rFontText.validateFontText(featureSlide);

						if (ruleFontText != null) {
							listRules.add(ruleFontText);
						}

						// REGLA 2:
						RuleSizeText rSizeText = new RuleSizeText();
						Rule ruleSizeText = rSizeText.validateSizeText(featureSlide);

						if (ruleSizeText != null) {
							listRules.add(ruleSizeText);
						}

						// REGLA 3:
						RuleItalics rItalics = new RuleItalics();
						Rule ruleItalics = rItalics.validateItalics(featureSlide);

						if (ruleItalics != null) {
							listRules.add(ruleItalics);
						}

						// REGLA 4:
						RuleBolds rBolds = new RuleBolds();
						Rule ruleBolds = rBolds.validateBolds(featureSlide);

						if (ruleBolds != null) {
							listRules.add(ruleBolds);
						}

						// REGLA 5:
						RuleUnderLine rUnderLine = new RuleUnderLine();
						Rule ruleUnderLine = rUnderLine.validateUnderLine(featureSlide);

						if (ruleUnderLine != null) {
							listRules.add(ruleUnderLine);
						}

						// REGLA 6:
						RuleShadowText rShadowText = new RuleShadowText();
						Rule ruleShadowText = rShadowText.validateShadowText(featureSlide);

						if (ruleShadowText != null) {
							listRules.add(ruleShadowText);
						}

						// REGLA 7:
						RuleUpperCase rUpperCase = new RuleUpperCase();
						Rule ruleUpperCase = rUpperCase.validateUpperCase(featureSlide);

						if (ruleUpperCase != null) {
							listRules.add(ruleUpperCase);
						}

						// REGLA 8:
						RuleColorText rColorText = new RuleColorText();
						Rule ruleColorText = rColorText.validateColorText(featureSlide);

						if (ruleColorText != null) {
							listRules.add(ruleColorText);
						}

						// REGLA 9:
						RuleColorBackground rColorBackground = new RuleColorBackground();
						Rule ruleColorBackground = rColorBackground.validateColorBackground(featureSlide);

						if (ruleColorBackground != null) {
							listRules.add(ruleColorBackground);
						}

						// REGLA 10:
						RuleMaxWords rMaxWords = new RuleMaxWords();
						Rule ruleMaxWords = rMaxWords.validateMaxWords(featureSlide);

						if (ruleMaxWords != null) {
							listRules.add(ruleMaxWords);
						}

						// REGLA 11:
						RuleSentenceMaxChars rSentenceMaxChars = new RuleSentenceMaxChars();
						Rule ruleSentenceMaxChars = rSentenceMaxChars.validateSentenceLength(featureSlide);

						if (ruleSentenceMaxChars != null) {
							listRules.add(ruleSentenceMaxChars);
						}

						// REGLA 12:
						RuleMaxNumber rMaxNumber = new RuleMaxNumber();
						Rule ruleMaxNumber = rMaxNumber.validateMaxNumber(featureSlide);

						if (ruleMaxNumber != null) {
							listRules.add(ruleMaxNumber);
						}

						// REGLA 13:
						RuleSpecialChars rSpecialChars = new RuleSpecialChars();
						Rule ruleSpecialChars = rSpecialChars.validateSpecialChars(featureSlide);

						if (ruleSpecialChars != null) {
							listRules.add(ruleSpecialChars);
						}

						// REGLA 14:
						RuleOrderChars rOrderChars = new RuleOrderChars();
						Rule ruleOrderChars = rOrderChars.validateOrderChars(featureSlide);

						if (ruleOrderChars != null) {
							listRules.add(ruleOrderChars);
						}

						// REGLA 15:
						RuleSentenceMaxWords rSentenceMaxWords = new RuleSentenceMaxWords();
						Rule ruleSentenceMaxWords = rSentenceMaxWords.validateSentenceMaxWords(featureSlide);

						if (ruleSentenceMaxWords != null) {
							listRules.add(ruleSentenceMaxWords);
						}

						// REGLA 16:
						RuleDateFormat rDateFormat = new RuleDateFormat();
						Rule ruleDateFormat = rDateFormat.validateDateFormat(featureSlide);

						if (ruleDateFormat != null) {
							listRules.add(ruleDateFormat);
						}

						// REGLA 17:
						RuleMaxPronoun rMaxPronoun = new RuleMaxPronoun();
						Rule ruleMaxPronoun = rMaxPronoun.validateMaxPronoun(featureSlide);

						if (ruleMaxPronoun != null) {
							listRules.add(ruleMaxPronoun);
						}

						// REGLA 18:
						RuleRomanNumber rRomanNumber = new RuleRomanNumber();
						Rule ruleRomanNumber = rRomanNumber.validateRomanNumber(featureSlide);

						if (ruleRomanNumber != null) {
							listRules.add(ruleRomanNumber);
						}

						// REGLA 19:
						RuleSecondPerson rSecondPerson = new RuleSecondPerson();
						Rule ruleSecondPerson = rSecondPerson.validateSecondPerson(featureSlide);

						if (ruleSecondPerson != null) {
							listRules.add(ruleSecondPerson);
						}

						// REGLA 20:
						RuleFormPasive rFormPasive = new RuleFormPasive();
						Rule ruleFormPasive = rFormPasive.validateFormPasive(featureSlide);

						if (ruleFormPasive != null) {
							listRules.add(ruleFormPasive);
						}

						// REGLA 21:
						RuleHasSubject rHasSubject = new RuleHasSubject();
						Rule ruleHasSubject = rHasSubject.validateHasSubject(featureSlide);

						if (ruleHasSubject != null) {
							listRules.add(ruleHasSubject);
						}

						// REGLA 22:
						RuleCorrectStructure rCorrectStructure = new RuleCorrectStructure();
						Rule ruleCorrectStructure = rCorrectStructure.validateCorrectStructure(featureSlide);

						if (ruleCorrectStructure != null) {
							listRules.add(ruleCorrectStructure);
						}

						// Serializamos la lista de reglas
						Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
						String ruleJson = prettyGson.toJson(listRules);

						HttpHeaders headers = new HttpHeaders();
						headers.add("Content-Type", "application/json;charset=UTF-8");

						logger.info("Se ha llevado a cabo la validación de la regla correctamente");
						return new ResponseEntity<>(ruleJson, headers, HttpStatus.OK);

					} else {

						logger.error("No se ha podido recuperar el contenido del documento solicitado");
						return new ResponseEntity<>(pMess.getProperty("cmu-errors.docContentNotFound"),
								HttpStatus.INTERNAL_SERVER_ERROR);

					}

				} else {

					logger.error("No se ha encontrado el documento");
					return new ResponseEntity<>(pMess.getProperty("cmu-errors.docNotFound"), HttpStatus.NOT_FOUND);

				}

			} else {
				logger.error("No se ha podido conectar con la base de datos");
				return new ResponseEntity<>(pMess.getProperty("cmu-errors.connectionError"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {

			logger.error("Se ha producido un error en el proceso de validación");
			return new ResponseEntity<>(pMess.getProperty("cmu-errors.validateRules"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/api/documents/{id_document}/satisfiedRules")
	@ResponseBody
	public ResponseEntity<String> validateSatisfiedRules(@PathVariable("id_document") String documentId)
			throws IOException {

		logger.info("Consulta las reglas superadas...");

		List<Rule> listRules = new ArrayList<>();

		// Cargamos los ficheros properties
		Properties pMess = loadProperties("analyzer.properties");

		try {

			// Nos conectamos a la bbdd y utilizamos la colección (definidas en
			// analyzer.properties)
			DBCollection collection = CreateConnectionProvider.createConnecion();

			if (collection != null) {

				ObjectId docID = new ObjectId(documentId);
				BasicDBObject d = new BasicDBObject();
				d.put("_id", docID);

				if (collection.getCount(d) == 1) {

					logger.info("Se ha encontrado el documento");

					if (collection.find(d).one().containsField("content")
							&& StringUtils.isNotBlank(collection.find(d).one().get("content").toString())) {

						// Recuperamos las caracteristicas de la diapositiva
						Slide featureSlide = AnalyzerHtmlProvider
								.analyzer(collection.find(d).one().get("content").toString());

						// REGLA 1:
						RuleFontText rFontText = new RuleFontText();
						Rule ruleFontText = rFontText.validateFontText(featureSlide);

						if (ruleFontText != null && ruleFontText.isPass()) {
							listRules.add(ruleFontText);
						}

						// REGLA 2:
						RuleSizeText rSizeText = new RuleSizeText();
						Rule ruleSizeText = rSizeText.validateSizeText(featureSlide);

						if (ruleSizeText != null && ruleSizeText.isPass()) {
							listRules.add(ruleSizeText);
						}

						// REGLA 3:
						RuleItalics rItalics = new RuleItalics();
						Rule ruleItalics = rItalics.validateItalics(featureSlide);

						if (ruleItalics != null && ruleItalics.isPass()) {
							listRules.add(ruleItalics);
						}

						// REGLA 4:
						RuleBolds rBolds = new RuleBolds();
						Rule ruleBolds = rBolds.validateBolds(featureSlide);

						if (ruleBolds != null && ruleBolds.isPass()) {
							listRules.add(ruleBolds);
						}

						// REGLA 5:
						RuleUnderLine rUnderLine = new RuleUnderLine();
						Rule ruleUnderLine = rUnderLine.validateUnderLine(featureSlide);

						if (ruleUnderLine != null && ruleUnderLine.isPass()) {
							listRules.add(ruleUnderLine);
						}

						// REGLA 6:
						RuleShadowText rShadowText = new RuleShadowText();
						Rule ruleShadowText = rShadowText.validateShadowText(featureSlide);

						if (ruleShadowText != null && ruleShadowText.isPass()) {
							listRules.add(ruleShadowText);
						}

						// REGLA 7:
						RuleUpperCase rUpperCase = new RuleUpperCase();
						Rule ruleUpperCase = rUpperCase.validateUpperCase(featureSlide);

						if (ruleUpperCase != null && ruleUpperCase.isPass()) {
							listRules.add(ruleUpperCase);
						}

						// REGLA 8:
						RuleColorText rColorText = new RuleColorText();
						Rule ruleColorText = rColorText.validateColorText(featureSlide);

						if (ruleColorText != null && ruleColorText.isPass()) {
							listRules.add(ruleColorText);
						}

						// REGLA 9:
						RuleColorBackground rColorBackground = new RuleColorBackground();
						Rule ruleColorBackground = rColorBackground.validateColorBackground(featureSlide);

						if (ruleColorBackground != null && ruleColorBackground.isPass()) {
							listRules.add(ruleColorBackground);
						}

						// REGLA 10:
						RuleMaxWords rMaxWords = new RuleMaxWords();
						Rule ruleMaxWords = rMaxWords.validateMaxWords(featureSlide);

						if (ruleMaxWords != null && ruleMaxWords.isPass()) {
							listRules.add(ruleMaxWords);
						}

						// REGLA 11:
						RuleSentenceMaxChars rSentenceMaxChars = new RuleSentenceMaxChars();
						Rule ruleSentenceMaxChars = rSentenceMaxChars.validateSentenceLength(featureSlide);

						if (ruleSentenceMaxChars != null && ruleSentenceMaxChars.isPass()) {
							listRules.add(ruleSentenceMaxChars);
						}

						// REGLA 12:
						RuleMaxNumber rMaxNumber = new RuleMaxNumber();
						Rule ruleMaxNumber = rMaxNumber.validateMaxNumber(featureSlide);

						if (ruleMaxNumber != null && ruleMaxNumber.isPass()) {
							listRules.add(ruleMaxNumber);
						}

						// REGLA 13:
						RuleSpecialChars rSpecialChars = new RuleSpecialChars();
						Rule ruleSpecialChars = rSpecialChars.validateSpecialChars(featureSlide);

						if (ruleSpecialChars != null && ruleSpecialChars.isPass()) {
							listRules.add(ruleSpecialChars);
						}

						// REGLA 14:
						RuleOrderChars rOrderChars = new RuleOrderChars();
						Rule ruleOrderChars = rOrderChars.validateOrderChars(featureSlide);

						if (ruleOrderChars != null && ruleOrderChars.isPass()) {
							listRules.add(ruleOrderChars);
						}

						// REGLA 15:
						RuleSentenceMaxWords rSentenceMaxWords = new RuleSentenceMaxWords();
						Rule ruleSentenceMaxWords = rSentenceMaxWords.validateSentenceMaxWords(featureSlide);

						if (ruleSentenceMaxWords != null && ruleSentenceMaxWords.isPass()) {
							listRules.add(ruleSentenceMaxWords);
						}

						// REGLA 16:
						RuleDateFormat rDateFormat = new RuleDateFormat();
						Rule ruleDateFormat = rDateFormat.validateDateFormat(featureSlide);

						if (ruleDateFormat != null && ruleDateFormat.isPass()) {
							listRules.add(ruleDateFormat);
						}

						// REGLA 17:
						RuleMaxPronoun rMaxPronoun = new RuleMaxPronoun();
						Rule ruleMaxPronoun = rMaxPronoun.validateMaxPronoun(featureSlide);

						if (ruleMaxPronoun != null && ruleMaxPronoun.isPass()) {
							listRules.add(ruleMaxPronoun);
						}

						// REGLA 18:
						RuleRomanNumber rRomanNumber = new RuleRomanNumber();
						Rule ruleRomanNumber = rRomanNumber.validateRomanNumber(featureSlide);

						if (ruleRomanNumber != null && ruleRomanNumber.isPass()) {
							listRules.add(ruleRomanNumber);
						}

						// REGLA 19:
						RuleSecondPerson rSecondPerson = new RuleSecondPerson();
						Rule ruleSecondPerson = rSecondPerson.validateSecondPerson(featureSlide);

						if (ruleSecondPerson != null && ruleSecondPerson.isPass()) {
							listRules.add(ruleSecondPerson);
						}

						// REGLA 20:
						RuleFormPasive rFormPasive = new RuleFormPasive();
						Rule ruleFormPasive = rFormPasive.validateFormPasive(featureSlide);

						if (ruleFormPasive != null && ruleFormPasive.isPass()) {
							listRules.add(ruleFormPasive);
						}

						// REGLA 21:
						RuleHasSubject rHasSubject = new RuleHasSubject();
						Rule ruleHasSubject = rHasSubject.validateHasSubject(featureSlide);

						if (ruleHasSubject != null && ruleHasSubject.isPass()) {
							listRules.add(ruleHasSubject);
						}

						// REGLA 22:
						RuleCorrectStructure rCorrectStructure = new RuleCorrectStructure();
						Rule ruleCorrectStructure = rCorrectStructure.validateCorrectStructure(featureSlide);

						if (ruleCorrectStructure != null && ruleCorrectStructure.isPass()) {
							listRules.add(ruleCorrectStructure);
						}

						// Serializamos la lista de reglas
						Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
						String ruleJson = prettyGson.toJson(listRules);

						HttpHeaders headers = new HttpHeaders();
						headers.add("Content-Type", "application/json;charset=UTF-8");

						logger.info("Se ha llevado a cabo la validación de la regla correctamente");
						return new ResponseEntity<>(ruleJson, headers, HttpStatus.OK);

					} else {

						logger.error("No se ha podido recuperar el contenido del documento solicitado");
						return new ResponseEntity<>(pMess.getProperty("cmu-errors.docContentNotFound"),
								HttpStatus.INTERNAL_SERVER_ERROR);

					}

				} else {

					logger.error("No se ha encontrado el documento");
					return new ResponseEntity<>(pMess.getProperty("cmu-errors.docNotFound"), HttpStatus.NOT_FOUND);

				}

			} else {
				logger.error("No se ha podido conectar con la base de datos");
				return new ResponseEntity<>(pMess.getProperty("cmu-errors.connectionError"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {

			logger.error("Se ha producido un error en el proceso de validación");
			return new ResponseEntity<>(pMess.getProperty("cmu-errors.validateRules"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/api/documents/{id_document}/notSatisfiedRules")
	@ResponseBody
	public ResponseEntity<String> validateNotSatisfiedRules(@PathVariable("id_document") String documentId)
			throws IOException {

		logger.info("Consulta las reglas superadas...");

		List<Rule> listRules = new ArrayList<>();

		// Cargamos los ficheros properties
		Properties pMess = loadProperties("analyzer.properties");

		try {

			// Nos conectamos a la bbdd y utilizamos la colección (definidas en
			// analyzer.properties)
			DBCollection collection = CreateConnectionProvider.createConnecion();

			if (collection != null) {

				ObjectId docID = new ObjectId(documentId);
				BasicDBObject d = new BasicDBObject();
				d.put("_id", docID);

				if (collection.getCount(d) == 1) {

					logger.info("Se ha encontrado el documento");

					if (collection.find(d).one().containsField("content")
							&& StringUtils.isNotBlank(collection.find(d).one().get("content").toString())) {

						// Recuperamos las caracteristicas de la diapositiva
						Slide featureSlide = AnalyzerHtmlProvider
								.analyzer(collection.find(d).one().get("content").toString());

						// REGLA 1:
						RuleFontText rFontText = new RuleFontText();
						Rule ruleFontText = rFontText.validateFontText(featureSlide);

						if (ruleFontText != null && !ruleFontText.isPass()) {
							listRules.add(ruleFontText);
						}

						// REGLA 2:
						RuleSizeText rSizeText = new RuleSizeText();
						Rule ruleSizeText = rSizeText.validateSizeText(featureSlide);

						if (ruleSizeText != null && !ruleSizeText.isPass()) {
							listRules.add(ruleSizeText);
						}

						// REGLA 3:
						RuleItalics rItalics = new RuleItalics();
						Rule ruleItalics = rItalics.validateItalics(featureSlide);

						if (ruleItalics != null && !ruleItalics.isPass()) {
							listRules.add(ruleItalics);
						}

						// REGLA 4:
						RuleBolds rBolds = new RuleBolds();
						Rule ruleBolds = rBolds.validateBolds(featureSlide);

						if (ruleBolds != null && !ruleBolds.isPass()) {
							listRules.add(ruleBolds);
						}

						// REGLA 5:
						RuleUnderLine rUnderLine = new RuleUnderLine();
						Rule ruleUnderLine = rUnderLine.validateUnderLine(featureSlide);

						if (ruleUnderLine != null && !ruleUnderLine.isPass()) {
							listRules.add(ruleUnderLine);
						}

						// REGLA 6:
						RuleShadowText rShadowText = new RuleShadowText();
						Rule ruleShadowText = rShadowText.validateShadowText(featureSlide);

						if (ruleShadowText != null && !ruleShadowText.isPass()) {
							listRules.add(ruleShadowText);
						}

						// REGLA 7:
						RuleUpperCase rUpperCase = new RuleUpperCase();
						Rule ruleUpperCase = rUpperCase.validateUpperCase(featureSlide);

						if (ruleUpperCase != null && !ruleUpperCase.isPass()) {
							listRules.add(ruleUpperCase);
						}

						// REGLA 8:
						RuleColorText rColorText = new RuleColorText();
						Rule ruleColorText = rColorText.validateColorText(featureSlide);

						if (ruleColorText != null && !ruleColorText.isPass()) {
							listRules.add(ruleColorText);
						}

						// REGLA 9:
						RuleColorBackground rColorBackground = new RuleColorBackground();
						Rule ruleColorBackground = rColorBackground.validateColorBackground(featureSlide);

						if (ruleColorBackground != null && !ruleColorBackground.isPass()) {
							listRules.add(ruleColorBackground);
						}

						// REGLA 10:
						RuleMaxWords rMaxWords = new RuleMaxWords();
						Rule ruleMaxWords = rMaxWords.validateMaxWords(featureSlide);

						if (ruleMaxWords != null && !ruleMaxWords.isPass()) {
							listRules.add(ruleMaxWords);
						}

						// REGLA 11:
						RuleSentenceMaxChars rSentenceMaxChars = new RuleSentenceMaxChars();
						Rule ruleSentenceMaxChars = rSentenceMaxChars.validateSentenceLength(featureSlide);

						if (ruleSentenceMaxChars != null && !ruleSentenceMaxChars.isPass()) {
							listRules.add(ruleSentenceMaxChars);
						}

						// REGLA 12:
						RuleMaxNumber rMaxNumber = new RuleMaxNumber();
						Rule ruleMaxNumber = rMaxNumber.validateMaxNumber(featureSlide);

						if (ruleMaxNumber != null && !ruleMaxNumber.isPass()) {
							listRules.add(ruleMaxNumber);
						}

						// REGLA 13:
						RuleSpecialChars rSpecialChars = new RuleSpecialChars();
						Rule ruleSpecialChars = rSpecialChars.validateSpecialChars(featureSlide);

						if (ruleSpecialChars != null && !ruleSpecialChars.isPass()) {
							listRules.add(ruleSpecialChars);
						}

						// REGLA 14:
						RuleOrderChars rOrderChars = new RuleOrderChars();
						Rule ruleOrderChars = rOrderChars.validateOrderChars(featureSlide);

						if (ruleOrderChars != null && !ruleOrderChars.isPass()) {
							listRules.add(ruleOrderChars);
						}

						// REGLA 15:
						RuleSentenceMaxWords rSentenceMaxWords = new RuleSentenceMaxWords();
						Rule ruleSentenceMaxWords = rSentenceMaxWords.validateSentenceMaxWords(featureSlide);

						if (ruleSentenceMaxWords != null && !ruleSentenceMaxWords.isPass()) {
							listRules.add(ruleSentenceMaxWords);
						}

						// REGLA 16:
						RuleDateFormat rDateFormat = new RuleDateFormat();
						Rule ruleDateFormat = rDateFormat.validateDateFormat(featureSlide);

						if (ruleDateFormat != null && !ruleDateFormat.isPass()) {
							listRules.add(ruleDateFormat);
						}

						// REGLA 17:
						RuleMaxPronoun rMaxPronoun = new RuleMaxPronoun();
						Rule ruleMaxPronoun = rMaxPronoun.validateMaxPronoun(featureSlide);

						if (ruleMaxPronoun != null && !ruleMaxPronoun.isPass()) {
							listRules.add(ruleMaxPronoun);
						}

						// REGLA 18:
						RuleRomanNumber rRomanNumber = new RuleRomanNumber();
						Rule ruleRomanNumber = rRomanNumber.validateRomanNumber(featureSlide);

						if (ruleRomanNumber != null && !ruleRomanNumber.isPass()) {
							listRules.add(ruleRomanNumber);
						}

						// REGLA 19:
						RuleSecondPerson rSecondPerson = new RuleSecondPerson();
						Rule ruleSecondPerson = rSecondPerson.validateSecondPerson(featureSlide);

						if (ruleSecondPerson != null && !ruleSecondPerson.isPass()) {
							listRules.add(ruleSecondPerson);
						}

						// REGLA 20:
						RuleFormPasive rFormPasive = new RuleFormPasive();
						Rule ruleFormPasive = rFormPasive.validateFormPasive(featureSlide);

						if (ruleFormPasive != null && !ruleFormPasive.isPass()) {
							listRules.add(ruleFormPasive);
						}

						// REGLA 21:
						RuleHasSubject rHasSubject = new RuleHasSubject();
						Rule ruleHasSubject = rHasSubject.validateHasSubject(featureSlide);

						if (ruleHasSubject != null && !ruleHasSubject.isPass()) {
							listRules.add(ruleHasSubject);
						}

						// REGLA 22:
						RuleCorrectStructure rCorrectStructure = new RuleCorrectStructure();
						Rule ruleCorrectStructure = rCorrectStructure.validateCorrectStructure(featureSlide);

						if (ruleCorrectStructure != null && !ruleCorrectStructure.isPass()) {
							listRules.add(ruleCorrectStructure);
						}

						// Serializamos la lista de reglas
						Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
						String ruleJson = prettyGson.toJson(listRules);

						HttpHeaders headers = new HttpHeaders();
						headers.add("Content-Type", "application/json;charset=UTF-8");

						logger.info("Se ha llevado a cabo la validación de la regla correctamente");
						return new ResponseEntity<>(ruleJson, headers, HttpStatus.OK);

					} else {

						logger.error("No se ha podido recuperar el contenido del documento solicitado");
						return new ResponseEntity<>(pMess.getProperty("cmu-errors.docContentNotFound"),
								HttpStatus.INTERNAL_SERVER_ERROR);

					}

				} else {

					logger.error("No se ha encontrado el documento");
					return new ResponseEntity<>(pMess.getProperty("cmu-errors.docNotFound"), HttpStatus.NOT_FOUND);

				}

			} else {
				logger.error("No se ha podido conectar con la base de datos");
				return new ResponseEntity<>(pMess.getProperty("cmu-errors.connectionError"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {

			logger.error("Se ha producido un error en el proceso de validación");
			return new ResponseEntity<>(pMess.getProperty("cmu-errors.validateRules"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/api/documents/{id_document}/checkRules")
	@ResponseBody
	public ResponseEntity<String> validateRulesById(@PathVariable("id_document") String documentId,
			@RequestParam("rules") List<Integer> rulesRequest) throws IOException {

		logger.info("Consulta las reglas superadas...");

		List<Rule> listRules = new ArrayList<>();

		// Cargamos los ficheros properties
		Properties pMess = loadProperties("analyzer.properties");

		if (rulesRequest != null && !rulesRequest.isEmpty()) {

			try {

				// Nos conectamos a la bbdd y utilizamos la colección (definidas en
				// analyzer.properties)
				DBCollection collection = CreateConnectionProvider.createConnecion();

				if (collection != null) {

					ObjectId docID = new ObjectId(documentId);
					BasicDBObject d = new BasicDBObject();
					d.put("_id", docID);

					if (collection.getCount(d) == 1) {

						logger.info("Se ha encontrado el documento");

						if (collection.find(d).one().containsField("content")
								&& StringUtils.isNotBlank(collection.find(d).one().get("content").toString())) {

							// Recuperamos las caracteristicas de la diapositiva
							Slide featureSlide = AnalyzerHtmlProvider
									.analyzer(collection.find(d).one().get("content").toString());

							// Eliminamos los identificadores de reglas repetidos, por si los hubiera..

							Set<Integer> hs = new HashSet<>();
							hs.addAll(rulesRequest);
							rulesRequest.clear();
							rulesRequest.addAll(hs);

							// Ordenamos los identificadores de menor a mayor para ir
							// anidando las reglas en función de su identificador:
							Collections.sort(rulesRequest);

							for (Integer rule : rulesRequest) {

								switch (rule) {

								case 1: // REGLA 1:
									RuleFontText rFontText = new RuleFontText();
									Rule ruleFontText = rFontText.validateFontText(featureSlide);

									if (ruleFontText != null) {
										listRules.add(ruleFontText);
									}

									break;

								case 2: // REGLA 2:
									RuleSizeText rSizeText = new RuleSizeText();
									Rule ruleSizeText = rSizeText.validateSizeText(featureSlide);

									if (ruleSizeText != null) {
										listRules.add(ruleSizeText);
									}

									break;

								case 3: // REGLA 3:
									RuleItalics rItalics = new RuleItalics();
									Rule ruleItalics = rItalics.validateItalics(featureSlide);

									if (ruleItalics != null) {
										listRules.add(ruleItalics);
									}

									break;

								case 4: // REGLA 4:
									RuleBolds rBolds = new RuleBolds();
									Rule ruleBolds = rBolds.validateBolds(featureSlide);

									if (ruleBolds != null) {
										listRules.add(ruleBolds);
									}

									break;

								case 5: // REGLA 5:
									RuleUnderLine rUnderLine = new RuleUnderLine();
									Rule ruleUnderLine = rUnderLine.validateUnderLine(featureSlide);

									if (ruleUnderLine != null) {
										listRules.add(ruleUnderLine);
									}

									break;

								case 6: // REGLA 6:
									RuleShadowText rShadowText = new RuleShadowText();
									Rule ruleShadowText = rShadowText.validateShadowText(featureSlide);

									if (ruleShadowText != null) {
										listRules.add(ruleShadowText);
									}

									break;

								case 7: // REGLA 7:
									RuleUpperCase rUpperCase = new RuleUpperCase();
									Rule ruleUpperCase = rUpperCase.validateUpperCase(featureSlide);

									if (ruleUpperCase != null) {
										listRules.add(ruleUpperCase);
									}

									break;

								case 8: // REGLA 8:
									RuleColorText rColorText = new RuleColorText();
									Rule ruleColorText = rColorText.validateColorText(featureSlide);

									if (ruleColorText != null) {
										listRules.add(ruleColorText);
									}

									break;

								case 9: // REGLA 9:
									RuleColorBackground rColorBackground = new RuleColorBackground();
									Rule ruleColorBackground = rColorBackground.validateColorBackground(featureSlide);

									if (ruleColorBackground != null) {
										listRules.add(ruleColorBackground);
									}

									break;

								case 10: // REGLA 10:
									RuleMaxWords rMaxWords = new RuleMaxWords();
									Rule ruleMaxWords = rMaxWords.validateMaxWords(featureSlide);

									if (ruleMaxWords != null) {
										listRules.add(ruleMaxWords);
									}

									break;

								case 11: // REGLA 11:
									RuleSentenceMaxChars rSentenceMaxChars = new RuleSentenceMaxChars();
									Rule ruleSentenceMaxChars = rSentenceMaxChars.validateSentenceLength(featureSlide);

									if (ruleSentenceMaxChars != null) {
										listRules.add(ruleSentenceMaxChars);
									}

									break;

								case 12: // REGLA 12:
									RuleMaxNumber rMaxNumber = new RuleMaxNumber();
									Rule ruleMaxNumber = rMaxNumber.validateMaxNumber(featureSlide);

									if (ruleMaxNumber != null) {
										listRules.add(ruleMaxNumber);
									}

									break;

								case 13: // REGLA 13:
									RuleSpecialChars rSpecialChars = new RuleSpecialChars();
									Rule ruleSpecialChars = rSpecialChars.validateSpecialChars(featureSlide);

									if (ruleSpecialChars != null) {
										listRules.add(ruleSpecialChars);
									}

									break;

								case 14: // REGLA 14:
									RuleOrderChars rOrderChars = new RuleOrderChars();
									Rule ruleOrderChars = rOrderChars.validateOrderChars(featureSlide);

									if (ruleOrderChars != null) {
										listRules.add(ruleOrderChars);
									}

									break;

								case 15: // REGLA 15:
									RuleSentenceMaxWords rSentenceMaxWords = new RuleSentenceMaxWords();
									Rule ruleSentenceMaxWords = rSentenceMaxWords
											.validateSentenceMaxWords(featureSlide);

									if (ruleSentenceMaxWords != null) {
										listRules.add(ruleSentenceMaxWords);
									}

									break;

								case 16: // REGLA 16:
									RuleDateFormat rDateFormat = new RuleDateFormat();
									Rule ruleDateFormat = rDateFormat.validateDateFormat(featureSlide);

									if (ruleDateFormat != null) {
										listRules.add(ruleDateFormat);
									}

									break;

								case 17: // REGLA 17:
									RuleMaxPronoun rMaxPronoun = new RuleMaxPronoun();
									Rule ruleMaxPronoun = rMaxPronoun.validateMaxPronoun(featureSlide);

									if (ruleMaxPronoun != null) {
										listRules.add(ruleMaxPronoun);
									}

									break;

								case 18: // REGLA 18:
									RuleRomanNumber rRomanNumber = new RuleRomanNumber();
									Rule ruleRomanNumber = rRomanNumber.validateRomanNumber(featureSlide);

									if (ruleRomanNumber != null) {
										listRules.add(ruleRomanNumber);
									}

									break;

								case 19:// REGLA 19:
									RuleSecondPerson rSecondPerson = new RuleSecondPerson();
									Rule ruleSecondPerson = rSecondPerson.validateSecondPerson(featureSlide);

									if (ruleSecondPerson != null) {
										listRules.add(ruleSecondPerson);
									}

									break;

								case 20: // REGLA 20:
									RuleFormPasive rFormPasive = new RuleFormPasive();
									Rule ruleFormPasive = rFormPasive.validateFormPasive(featureSlide);

									if (ruleFormPasive != null) {
										listRules.add(ruleFormPasive);
									}

									break;

								case 21: // REGLA 21:
									RuleHasSubject rHasSubject = new RuleHasSubject();
									Rule ruleHasSubject = rHasSubject.validateHasSubject(featureSlide);

									if (ruleHasSubject != null) {
										listRules.add(ruleHasSubject);
									}

									break;

								case 22: // REGLA 22:
									RuleCorrectStructure rCorrectStructure = new RuleCorrectStructure();
									Rule ruleCorrectStructure = rCorrectStructure
											.validateCorrectStructure(featureSlide);

									if (ruleCorrectStructure != null) {
										listRules.add(ruleCorrectStructure);
									}

									break;

								default:
									logger.error("No se encuentra la regla con identificador: " + rule);
									return new ResponseEntity<>(
											pMess.getProperty("cmu-errors.notRuleFound") + " " + rule,
											HttpStatus.NOT_FOUND);

								}
							}

							// Serializamos la lista de reglas
							Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
							String ruleJson = prettyGson.toJson(listRules);

							HttpHeaders headers = new HttpHeaders();
							headers.add("Content-Type", "application/json;charset=UTF-8");

							logger.info("Se ha llevado a cabo la validación de la regla correctamente");
							return new ResponseEntity<>(ruleJson, headers, HttpStatus.OK);

						} else {

							logger.error("No se ha podido recuperar el contenido del documento solicitado");
							return new ResponseEntity<>(pMess.getProperty("cmu-errors.docContentNotFound"),
									HttpStatus.INTERNAL_SERVER_ERROR);

						}

					} else {

						logger.error("No se ha encontrado el documento");
						return new ResponseEntity<>(pMess.getProperty("cmu-errors.docNotFound"), HttpStatus.NOT_FOUND);

					}

				} else {
					logger.error("No se ha podido conectar con la base de datos");
					return new ResponseEntity<>(pMess.getProperty("cmu-errors.connectionError"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} catch (Exception e) {

				logger.error("Se ha producido un error en el proceso de validación");
				return new ResponseEntity<>(pMess.getProperty("cmu-errors.validateRules"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			logger.error("No se ha recibido ningún parámetro");
			return new ResponseEntity<>(pMess.getProperty("cmu-errors.notParameteres"), HttpStatus.BAD_REQUEST);
		}

	}
}
