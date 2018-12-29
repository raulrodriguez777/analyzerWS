package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla UpperCase: No se permite tener más de 5 palabras en mayúscula
 * 
 * @author Raúl
 *
 */
public class RuleUpperCase extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleUpperCase.class);
	private static final String DEFINITIONRULE = "rules.RuleUpperCase";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateUpperCase(Slide fSlide) throws IOException {

		logger.info("Consulta regla mayúsculas...");

		Rule ruleUpperCase = allOK(DEFINITIONRULE);

		if (ruleUpperCase != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxUpperCase"))) {

				try {

					int maxUpperCase = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxUpperCase"));

					if (fSlide.getNumUpperCase() > maxUpperCase) {

						ruleUpperCase.setPass(false);
						ruleUpperCase.setReason(
								"El documento contiene " + fSlide.getNumUpperCase() + " palabras en mayúscula");

					}

					return ruleUpperCase;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de mayúsculas permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de mayúsculas permitidas");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
