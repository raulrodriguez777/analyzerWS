package com.analyzer.html.rules;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla specialChars: No se permite el uso de caracteres especiales
 * 
 * @author Raúl
 *
 */
public class RuleSpecialChars extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleSpecialChars.class);
	private static final String DEFINITIONRULE = "rules.RuleSpecialChars";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateSpecialChars(Slide fSlide) throws IOException {

		logger.info("Consulta regla carácteres especiales...");

		Rule ruleSpecialChars = allOK(DEFINITIONRULE);

		if (ruleSpecialChars != null) {

			if (!fSlide.getSpecialChars().isEmpty()) {

				ruleSpecialChars.setPass(false);
				ruleSpecialChars.setReason("El documento contiene los siguientes caracteres no permitidos: "
						+ fSlide.getSpecialChars().toString());

			}

			return ruleSpecialChars;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
