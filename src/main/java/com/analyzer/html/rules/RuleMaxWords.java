package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla wordsNumber: No se permite tener más de 50 palabras en toda la
 * diapositiva diapositiva
 * 
 * @author Raúl
 *
 */
public class RuleMaxWords extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleMaxWords.class);
	private static final String DEFINITIONRULE = "rules.RuleMaxWords";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateMaxWords(Slide fSlide) throws IOException {

		logger.info("Consulta regla Número máximo Palabras...");

		Rule ruleMaxWords = allOK(DEFINITIONRULE);

		if (ruleMaxWords != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxWords"))) {

				try {

					int maxWords = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxWords"));

					if (fSlide.getNumWords() > maxWords) {

						ruleMaxWords.setPass(false);
						ruleMaxWords.setReason("El documento contiene " + fSlide.getNumWords() + " palabras");

					}

					return ruleMaxWords;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de palabras permitido");
					return null;
				}

			}

			else {
				logger.error("No está definido el número máximo de palabras permitidas");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
