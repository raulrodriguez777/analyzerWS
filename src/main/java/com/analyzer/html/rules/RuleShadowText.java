package com.analyzer.html.rules;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla ShadowText: No se permite tener texto con sombreado
 * 
 * @author Raúl
 *
 */
public class RuleShadowText extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleShadowText.class);
	private static final String DEFINITIONRULE = "rules.RuleShadowText";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateShadowText(Slide fSlide) throws IOException {

		logger.info("Consulta regla texto sombreado...");

		Rule ruleShadowText = allOK(DEFINITIONRULE);

		if (ruleShadowText != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxShadow"))) {

				try {

					int maxShadow = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxShadow"));

					if (fSlide.getNumShadowWords() > maxShadow) {

						ruleShadowText.setPass(false);
						ruleShadowText.setReason(
								"El documento contiene " + fSlide.getNumShadowWords() + " palabras sombreadas");

					}

					return ruleShadowText;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de sombreadas permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de sombreadas permitidas");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
