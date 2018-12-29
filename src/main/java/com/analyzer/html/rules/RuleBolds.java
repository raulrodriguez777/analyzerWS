package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla bolds: No se permite tener más de 5 palabras en negrita
 * 
 * @author Raúl
 *
 */
public class RuleBolds extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleBolds.class);
	private static final String DEFINITIONRULE = "rules.RuleBolds";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateBolds(Slide fSlide) throws IOException {

		logger.info("Consulta regla negritas...");

		Rule ruleBolds = allOK(DEFINITIONRULE);

		if (ruleBolds != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxBolds"))) {

				try {

					int maxBolds = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxBolds"));

					if (fSlide.getNumBold() > maxBolds) {

						ruleBolds.setPass(false);
						ruleBolds.setReason("El documento contiene " + fSlide.getNumBold() + " palabras en negrita");

					}

					return ruleBolds;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de negritas permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de negritas permitidas");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
