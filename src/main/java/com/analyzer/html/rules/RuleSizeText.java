package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla SizeWord: No se permite tener un tamaño de texto menor de 12 ni mayor
 * de 16
 * 
 * @author Raúl
 *
 */
public class RuleSizeText extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleSizeText.class);
	private static final String DEFINITIONRULE = "rules.RuleSizeText";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateSizeText(Slide fSlide) throws IOException {

		List<String> notAcceptedSize = new ArrayList<>();

		logger.info("Validando la regla del tamaño del texto");

		Rule ruleSizeText = allOK(DEFINITIONRULE);

		if (ruleSizeText != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".minimumSize"))
					&& StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maximunSize"))) {

				try {

					int minimumSize = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".minimumSize"));
					int maximunSize = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maximunSize"));

					for (String sizeText : fSlide.getSizeText()) {

						int size = Integer.parseInt(sizeText.substring(0, sizeText.length() - 2));
						if (size < minimumSize || size > maximunSize) {

							notAcceptedSize.add(String.valueOf(size));

						}

					}
					if (!notAcceptedSize.isEmpty()) {

						ruleSizeText.setPass(false);
						ruleSizeText.setReason("El documento presenta los siguientes tamaños no permitidos: "
								+ notAcceptedSize.toString());
					}

					return ruleSizeText;

				} catch (Exception e) {
					logger.error("Se ha producido un error al intentar parsear los tamaños del texto");
					return null;
				}

			} else {
				logger.error("No están definidos los límites de tamaño máximo para la regla en el fichero properties");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
