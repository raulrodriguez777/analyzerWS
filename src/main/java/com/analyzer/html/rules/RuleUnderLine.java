package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla UnderLine: No se permite tener más de 2 palabras subrayadas
 * 
 * @author Raúl
 *
 */
public class RuleUnderLine extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleUnderLine.class);
	private static final String DEFINITIONRULE = "rules.RuleUnderLine";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateUnderLine(Slide fSlide) throws IOException {

		logger.info("Consulta regla subrayadas...");

		Rule ruleUnderLine = allOK(DEFINITIONRULE);

		if (ruleUnderLine != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxUnderLine"))) {

				try {

					int maxUnderLine = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxUnderLine"));

					if (fSlide.getNumUnderline() > maxUnderLine) {

						ruleUnderLine.setPass(false);
						ruleUnderLine.setReason(
								"El documento contiene " + fSlide.getNumUnderline() + " palabras subrayadas");

					}

					return ruleUnderLine;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de subrayadas permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de subrayadas permitidas");
				return null;
			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}
}
