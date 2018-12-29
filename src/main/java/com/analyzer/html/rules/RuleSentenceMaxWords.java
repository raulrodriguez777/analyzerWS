package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;

/**
 * Regla sentenceMaxWords: Las frases no pueden contener más de 15 palabras
 * 
 * @author Raúl
 *
 */
public class RuleSentenceMaxWords extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleSentenceMaxWords.class);
	private static final String DEFINITIONRULE = "rules.RuleSentenceMaxWords";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateSentenceMaxWords(Slide fSlide) throws IOException {

		logger.info("Consulta regla número palabras en frase...");

		Rule ruleSentenceLength = allOK(DEFINITIONRULE);

		if (ruleSentenceLength != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxWordsSentence"))) {

				try {

					int maxSentenceWords = Integer
							.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxWordsSentence"));

					if (!fSlide.getListSentences().isEmpty()) {

						List<String> incorrectSentences = new ArrayList<>();

						for (Sentence sentence : fSlide.getListSentences()) {

							if (sentence.getNumWords() > maxSentenceWords) {

								incorrectSentences.add(sentence.getContent());
							}
						}

						if (!incorrectSentences.isEmpty()) {

							ruleSentenceLength.setPass(false);
							ruleSentenceLength.setReason(
									"El documento contiene las siguientes frases con mayor número de palabras de lo permitido: "
											+ incorrectSentences.toString());
						}

					}

					return ruleSentenceLength;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de palabras por frase permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de palabras por frase permitidas");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
