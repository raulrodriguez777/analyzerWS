package com.analyzer.html.rules;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla romanNumber: No se permite el uso de números romanos
 * 
 * @author Raúl
 *
 */
public class RuleRomanNumber extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleRomanNumber.class);
	private static final String DEFINITIONRULE = "rules.RuleRomanNumber";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateRomanNumber(Slide fSlide) throws IOException {

		logger.info("Consulta regla Números Romanos...");

		Rule ruleRomanNumber = allOK(DEFINITIONRULE);

		if (ruleRomanNumber != null) {

			if (!fSlide.getRomanNumber().isEmpty()) {

				ruleRomanNumber.setPass(false);
				ruleRomanNumber.setReason("El documento contiene los siguientes números romanos: "
						+ fSlide.getRomanNumber().toString());

			}

			return ruleRomanNumber;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
