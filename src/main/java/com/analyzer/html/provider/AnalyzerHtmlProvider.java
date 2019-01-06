package com.analyzer.html.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.analyzer.html.vo.Data;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;
import com.analyzer.html.vo.TokenList;

public class AnalyzerHtmlProvider {

	private static Logger logger = Logger.getLogger(AnalyzerHtmlProvider.class);

	public static Slide analyzer(String html) {

		logger.info("Creando el objeto Slide...");

		Slide featuresSlide = new Slide();

		try {

			Document doc = Jsoup.parse(html);

			// 1. Se añaden todas las fuentes de texto:
			featuresSlide.setFonts(getValuesAttribute(doc, "font-family"));

			// 2. Se añaden todos los tamaños de letra::
			featuresSlide.setSizeText(getValuesAttribute(doc, "font-size"));

			// 3. Se añaden las cursivas
			featuresSlide.setNumItalics(getItalics(doc));

			// 4. Se añaden las negritas
			featuresSlide.setNumBold(getBolds(doc));

			// 5. Se añaden las subrayadas
			featuresSlide.setNumUnderline(getUnderLine(doc));

			// 6. Se añaden las sombreadas
			featuresSlide.setNumShadowWords(numWordsStyle(doc, "text-shadow"));

			// Todas las reglas que necesitan expresiones regulares se construyen juntas:
			// 7 (mayúsculas), 12 (números mayores de 100.00), 16 (Fechas formato
			// incorrecto) y 18 (nº romanos)

			attributesByRegularExpression(doc, featuresSlide);

			// 8. Se añaden todos los colores del texto
			featuresSlide.setColors(getValuesAttribute(doc, "color"));

			// 9. Se añaden todos los colores de fondo
			featuresSlide.setBackgroundColors(getBackGroundColors(doc));

			// 10. Se añade el número total de palabras:
			StringTokenizer words = new StringTokenizer(doc.body().text(), " ");
			featuresSlide.setNumWords(words.countTokens());

			// Se añaden las frases que contiene el documento:
			// Las frases se necesitan para validar las reglas 11,15,17,19,20,21 y 22

			featuresSlide.setListSentences(getSentences(doc));

			// 13. Se añade los caracteres especiales que contiene
			featuresSlide.setSpecialChars(getSpecialChars(doc));

			// 14. Se añade los caracteres de orden que contiene
			featuresSlide.setOrderChars(getOrderChars(doc));

		} catch (Exception e) {
			// Error al parsear HTML
			logger.error("Error al parsear el documento", e);
			return null;
		}

		return featuresSlide;
	}

	/**
	 * 
	 * @param doc Diapositiva
	 * @return Número de palabras cursivas que tiene la diapositiva
	 */
	private static int getItalics(Document doc) {

		// cursivas con etiqueta "em" + cursivas con etiqueta "i" + cursivas con
		// atributo style="font-style: italic"

		return numWordsTag(doc, "em") + numWordsTag(doc, "i") + numWordsStyle(doc, "font-style: italic");

	}

	/**
	 * 
	 * @param doc Diapositiva
	 * @return Número de palabras en negrita que tiene la diapositiva
	 */
	private static int getBolds(Document doc) {

		// negritas con etiqueta "strong" + negritas con etiqueta "b" + negritas con
		// atributo style="font-style: bold"

		return numWordsTag(doc, "strong") + numWordsTag(doc, "b") + numWordsStyle(doc, "font-style: bold");

	}

	/**
	 * 
	 * @param doc Diapositiva
	 * @return Número de palabras subrayadas que tiene la diapositiva
	 */
	private static int getUnderLine(Document doc) {

		// subrayadas con etiqueta "u" + subrayadas con atributo style="text-decoration:
		// underline"
		return numWordsTag(doc, "u") + numWordsStyle(doc, "text-decoration: underline");

	}

	/**
	 * 
	 * @param doc Diapositiva
	 * @param tag Etiqueta a buscar en la diapositiva
	 * @return Devuelve el número de palabras que tienen la etiqueta pasada como
	 *         parámetro
	 */
	private static int numWordsTag(Document doc, String tag) {

		Iterator<Element> it;
		int numWords = 0;

		// Se buscan las palabras con la etiqueta recibida
		Elements words = doc.select(tag);
		if (!words.isEmpty()) {

			it = words.iterator();

			while (it.hasNext()) {

				Element element = it.next();

				if (element.hasText()) {
					StringTokenizer numTokens = new StringTokenizer(element.text(), " ");
					numWords += numTokens.countTokens();
				}
			}
		}

		return numWords;

	}

	/**
	 * 
	 * @param doc        Diapositiva
	 * @param valueStyle Cadena a buscar en el atributo style
	 * @return Devuelve el número de palabras con un valor de style que contenga la
	 *         cadena pasada en el parametro valueStyle
	 */
	private static int numWordsStyle(Document doc, String valueStyle) {

		Iterator<Element> it;
		int numWords = 0;

		Elements e = doc.getElementsByAttributeValueContaining("style", valueStyle);

		if (!e.isEmpty()) {

			it = e.iterator();

			while (it.hasNext()) {

				Element element = it.next();

				if (element.hasText()) {

					StringTokenizer numTokens = new StringTokenizer(element.text(), " ");
					numWords += numTokens.countTokens();
				}

			}

		}
		return numWords;
	}

	/**
	 * 
	 * @param doc       Diapositiva a analizar
	 * @param attribute Atributo a buscar en cada elemento de la diapositiva
	 * @return Una lista con todos los valores de la diapositiva para el atributo
	 *         pasado como parámetro
	 */
	private static List<String> getValuesAttribute(Document doc, String attribute) {

		List<String> values = new ArrayList<>();
		Iterator<Element> it;

		Elements e = doc.getElementsByAttributeValueContaining("style", attribute);

		if (!e.isEmpty()) {

			it = e.iterator();

			while (it.hasNext()) {

				Element element = it.next();

				if (element.hasText()) {

					String[] styleAttributes = element.attr("style").split(";");

					for (int i = 0; i < styleAttributes.length; i++) {

						if (attribute.equals("font-family")) {

							String[] attrVal = styleAttributes[i].split(":")[1].trim().split(",");

							if (!values.contains(attrVal[0])) {
								values.add(attrVal[0].trim());
							}

						} else {

							String[] attrVal = styleAttributes[i].split(":");

							if (attrVal[0].trim().equalsIgnoreCase(attribute) && !values.contains(attrVal[1].trim())) {

								values.add(attrVal[1].trim());

							}
						}
					}
				}

			}

		}

		return values;
	}

	/**
	 * 
	 * @param doc Diapositiva
	 * @return Lista con todos los colores encontrados como valores de los atributos
	 *         background y background-color para la diapositiva pasada como
	 *         parámetro
	 */
	private static List<String> getBackGroundColors(Document doc) {

		List<String> bacgroundColors = new ArrayList<>();

		List<String> backgroundList = getValuesAttribute(doc, "background");

		if (!backgroundList.isEmpty()) {

			bacgroundColors.addAll(backgroundList);

		}

		List<String> backgroundCList = getValuesAttribute(doc, "background-color");

		if (!backgroundCList.isEmpty()) {

			bacgroundColors.addAll(backgroundCList);

		}

		return bacgroundColors;
	}

	/**
	 * Método donde se obtienen los caracteres especiales que incluye el documento
	 * 
	 * @param doc La diapositiva a analizar
	 * @return Lista con los caracteres especiales que incluye el documento
	 * @throws IOException
	 */
	private static List<String> getSpecialChars(Document doc) throws IOException {

		List<String> specialChars = getSpecialChars();
		List<String> charsInDoc = new ArrayList<>();

		for (String specialC : specialChars) {

			if (doc.body().text().contains(specialC)) {
				charsInDoc.add(specialC);
			}
		}

		return charsInDoc;
	}

	/**
	 * Carga del fichero properties
	 * 
	 * @return fichero properties cargado
	 * @throws IOException
	 * 
	 */
	private static Properties loadProperties(String file) throws IOException {

		logger.info("Cargando fichero: " + file);

		// Cargamos el fichero properties
		PropertyValuesProvider properties = new PropertyValuesProvider();
		return properties.getPropertiesValues(file);

	}

	/**
	 * Método para recuperar la lista de caracteres especiales definidos en el
	 * fichero properties
	 * 
	 * @return La lista de caracteres especiales definidos
	 * @throws IOException
	 */
	private static List<String> getSpecialChars() throws IOException {

		logger.info("Consulta caracteres especiales definidos en el fichero rule-definition.properties...");

		List<String> specialChars = new ArrayList<>();

		// Carga del fichero properties
		Properties pRules = loadProperties("rules-definition.properties");
		int ruleId = Integer.parseInt(pRules.getProperty("rules.RuleSpecialChars").split("-")[0]);

		if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".specialChars"))) {

			String[] chars = pRules.getProperty("rules.r" + ruleId + ".specialChars").split(",");

			for (int i = 0; i < chars.length; i++) {

				if (!specialChars.contains(chars[i].toUpperCase().trim())) {

					specialChars.add(chars[i].toUpperCase().trim());

				}

			}

		}

		return specialChars;
	}

	/**
	 * Método donde se obtienen los caracteres de orden que incluye el documento
	 * 
	 * @param doc La diapositiva a analizar
	 * @return Lista con los caracteres de orden que incluye el documento
	 * @throws IOException
	 */
	private static List<String> getOrderChars(Document doc) throws IOException {

		List<String> orderChars = getOrderChars();
		List<String> charsInDoc = new ArrayList<>();

		for (String specialC : orderChars) {

			if (doc.body().text().contains(specialC)) {
				charsInDoc.add(specialC);
			}
		}

		return charsInDoc;
	}

	/**
	 * Método para recuperar la lista de caracteres de orden definidos en el fichero
	 * properties
	 * 
	 * @return La lista de caracteres de orden definidos
	 * @throws IOException
	 */
	private static List<String> getOrderChars() throws IOException {

		logger.info("Consulta caracteres de orden definidos en el fichero rule-definition.properties...");

		List<String> orderChars = new ArrayList<>();

		// Carga del fichero properties
		Properties pRules = loadProperties("rules-definition.properties");
		int ruleId = Integer.parseInt(pRules.getProperty("rules.RuleOrderChars").split("-")[0]);

		if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".orderChars"))) {

			String[] chars = pRules.getProperty("rules.r" + ruleId + ".orderChars").split(",");

			for (int i = 0; i < chars.length; i++) {

				if (!orderChars.contains(chars[i].toUpperCase().trim())) {

					orderChars.add(chars[i].toUpperCase().trim());

				}

			}

		}

		return orderChars;
	}

	/**
	 * Este metodo completa los atributos de la diapositiva que necesitan de
	 * expresiones regulares (Necesarios para las reglas 7, 12, 16 y 18)
	 * 
	 * @param doc
	 * @param s
	 */
	private static void attributesByRegularExpression(Document doc, Slide s) {

		// Lista con los numeros encontrados en la diapositiva:
		List<Double> numbers = new ArrayList<>();
		// Contador con el número de palabras en mayúscula:
		int cntUppercase = 0;
		// Lista con las fechas con formato incorrecto encontradas:
		List<String> wrongDateFormat = new ArrayList<>();
		// Lista con los números romanos encontrados:
		List<String> romansNum = new ArrayList<>();

		StringTokenizer words = new StringTokenizer(doc.body().text(), " ");

		while (words.hasMoreTokens()) {

			String word = words.nextToken();

			// Si encontramos un número, se añade a la lista de números
			String word1 = word.replace(".", "");
			
			String word2 = word1.replace(",","");

			if (word1.matches("^[0-9]+([,][0-9]*?)?$")) {

				word1 = word1.replace(",", ".");
				numbers.add(Double.parseDouble(word1));

			}
			// Si encontramos un número romano, se añade a la lista de los números romanos:
			else if (word2.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$")) {
				romansNum.add(word2);
			}
			// Si encontramos una palabra en mayúsculas (y esta no es un número romano), se
			// incrementa el contador de mayúsculas
			else if (word2.matches("^[A-Z,Á,É,Í,Ó,Ú]*")
					&& !word2.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$")) {

				cntUppercase++;

			}
			// Si encontramos una fecha en este formato, se añade a la lista de fechas
			// incorrectas
			else if (word2.matches("^\\d{2}-\\d{2}-\\d{4}$") || word2.matches("^\\d{4}-\\d{2}-\\d{2}$")
					|| word2.matches("^\\d{2}/\\d{2}/\\d{4}$") || word2.matches("^\\d{4}/\\d{2}/\\d{2}$")) {

				wrongDateFormat.add(word2);
			}

		}

		// Se añaden los números encontrados
		s.setNumbers(numbers);
		s.setNumUpperCase(cntUppercase);
		s.setWrongDateFormat(wrongDateFormat);
		s.setRomanNumber(romansNum);
	}

	/**
	 * Método donde se obtienen todas las frases que componen la diapositiva, cada
	 * una con sus correspondientes caracteristicas
	 * 
	 * @param doc Diapositiva a analizar
	 * @return Listado con las frases que incluyen más de 60 caracteres
	 */
	private static List<Sentence> getSentences(Document doc) {

		List<Sentence> sentences = new ArrayList<>();
		List<Integer> positionFinalSentence = new ArrayList<>();

		String originalText = doc.body().text();
		char[] charsText = originalText.toCharArray();

		for (int i = 0; i < charsText.length; i++) {

			if ((charsText[i] == ".".charAt(0) && charsText[i - 1] != ".".charAt(0)
					&& ((i + 1 == charsText.length)
							|| (charsText[i + 1] != ".".charAt(0) && charsText[i + 1] != ",".charAt(0))))
					|| charsText[i] == "?".charAt(0) || charsText[i] == "!".charAt(0)) {
				positionFinalSentence.add(i);
			}

		}
		if (!positionFinalSentence.contains(charsText.length - 1)) {

			positionFinalSentence.add(charsText.length - 1);
		}

		if (!positionFinalSentence.isEmpty()) {

			Integer[] poskArr = new Integer[positionFinalSentence.size()];
			poskArr = positionFinalSentence.toArray(poskArr);

			for (int i = 0; i < poskArr.length; i++) {
				String frase = "";
				if (i == 0) {

					frase = originalText.substring(0, poskArr[i]);

				} else {

					frase = originalText.substring(poskArr[i - 1] + 1, poskArr[i] + 1);
				}

				// Llamamos al método morphologicalAnalysis, que recibe el texto de la frase
				// y lo envía a la api encargada del análisis morfológico:

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.error("Error al pausar el proceso", e);
				}
				Sentence objectSentence = morphologicalAnalysis(frase);

				// Utilizamos el objeto recibido para completar el resto de sus atributos:

				// Añadimos el contenido de la frase
				objectSentence.setContent(frase);
				// Añadimos el número de caracteres de la frase
				objectSentence.setNumChars(frase.replace(" ", "").toCharArray().length);
				// Añadimos el número de palabras de la frase
				objectSentence.setNumWords(new StringTokenizer(frase, " ").countTokens());

				sentences.add(objectSentence);

			}

		}

		return sentences;
	}

	/**
	 * Método que completa los atributos del objeto Sentence referentes al análisis
	 * morfológico, para ello utiliza la clase MorphologicalAnalyzerProvider donde
	 * se implementa la llamada a la api encargada de ello, en este método se recibe
	 * el json deserializado respuesta de la api y se trata
	 * 
	 * @param textos Texto de la frase
	 * @return Objeto Sentence con los atributos correspondientes completados
	 */
	private static Sentence morphologicalAnalysis(String text) {

		int contPronoun = 0;
		boolean secondPerson = true;
		boolean formActive = true;
		boolean haveSubject = false;

		Sentence s = new Sentence();

		Data response = MorphologicalAnalyzerProvider.morphologicalAnalyzer(text);

		if (response != null) {

			Map<String, List<String>> m = new HashMap<>();

			findProperties(response.getToken_list().get(0), m);

			// En este momento, ya tenemos todos los atributos necesarios
			// tag_info = Lista con los valores para el tipo de palabra
			// syntactic_tree_relation_list = Lista con las relaciones sintácticas

			if (m.containsKey("tag_info")) {

				List<String> valuesTag = m.get("tag_info");
				if (valuesTag != null && !valuesTag.isEmpty()) {

					for (String value : valuesTag) {

						if (value.equalsIgnoreCase("RELATIVO") || value.equalsIgnoreCase("CUANTIFICADOR")
								|| value.equalsIgnoreCase("POSESIVO") || value.equalsIgnoreCase("DEMOSTRATIVO")
								|| value.equalsIgnoreCase("INTERROGATIVO") || value.equalsIgnoreCase("PERSONAL")
								|| value.equalsIgnoreCase("NUMERAL")) {
							contPronoun++;
						}

						if (value.contains("1") || value.contains("3")) {
							secondPerson = false;
						}

						if (value.equalsIgnoreCase("PASIVA")) {

							formActive = false;

						}

						if (value.equalsIgnoreCase("SUJETO")) {
							haveSubject = true;
						}

					}

				}

			}
			if (m.containsKey("syntactic_tree_relation_list")) {

				List<String> valuesTag = m.get("tag_info");
				if (valuesTag != null && !valuesTag.isEmpty()) {

					for (String value : valuesTag) {

						if (value.equalsIgnoreCase("isSubject")) {
							haveSubject = true;
						}
					}

				}

			}

		}

		s.setNumPronoun(contPronoun);
		s.setFormActive(formActive);
		s.setHaveSubject(haveSubject);
		s.setSecondPerson(secondPerson);

		return s;
	}

	private static void findProperties(TokenList tl, Map<String, List<String>> m) {

		if (tl.getAnalysis_list() != null && !tl.getAnalysis_list().isEmpty()) {

			List<String> tagList = new ArrayList<>();

			String valueTags = tl.getAnalysis_list().get(0).getTag_info();

			if (StringUtils.isNotBlank(valueTags)) {

				String[] values = valueTags.split(",");

				for (int i = 0; i < values.length; i++) {

					tagList.add(values[i].trim());

				}

			}

			if (m.containsKey("tag_info")) {

				m.get("tag_info").addAll(tagList);
			}

			else {

				m.put("tag_info", tagList);
			}

		}
		if (tl.getSyntactic_tree_relation_list() != null && !tl.getSyntactic_tree_relation_list().isEmpty()) {

			List<String> relationList = new ArrayList<>();

			String valueTags = tl.getSyntactic_tree_relation_list().get(0).getType();

			if (StringUtils.isNotBlank(valueTags)) {

				String[] values = valueTags.split(",");

				for (int i = 0; i < values.length; i++) {

					relationList.add(values[i].trim());

				}
			}

			if (m.containsKey("syntactic_tree_relation_list")) {

				m.get("syntactic_tree_relation_list").addAll(relationList);
			}

			else {

				m.put("syntactic_tree_relation_list", relationList);
			}

		}

		if (tl.getToken_list() != null && !tl.getToken_list().isEmpty()) {

			for (TokenList token : tl.getToken_list()) {

				findProperties(token, m);

			}

		}

	}

}
