package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla Italics: No se permite tener texto en cursiva
 * 
 * @author Raúl
 *
 */
public class RuleItalics extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleItalics.class);
	private static final String DEFINITIONRULE = "rules.RuleItalics";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateItalics(Slide fSlide) throws IOException {

		logger.info("Consulta regla cursivas...");

		Rule ruleItalics = allOK(DEFINITIONRULE);

		if (ruleItalics != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxItalics"))) {

				try {

					int maxItalics = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxItalics"));

					if (fSlide.getNumItalics() > maxItalics) {

						ruleItalics.setPass(false);
						ruleItalics
								.setReason("El documento contiene " + fSlide.getNumItalics() + " palabras en cursiva");

					}

					return ruleItalics;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de cursivas permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de cursivas permitidas");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
