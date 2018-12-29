package com.analyzer.html.rules;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla orderChars: No se permite el uso de caracteres de orden
 * 
 * @author Raúl
 *
 */
public class RuleOrderChars extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleOrderChars.class);
	private static final String DEFINITIONRULE = "rules.RuleOrderChars";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateOrderChars(Slide fSlide) throws IOException {

		logger.info("Consulta regla carácteres de orden...");

		Rule ruleOrderChars = allOK(DEFINITIONRULE);

		if (ruleOrderChars != null) {

			if (!fSlide.getOrderChars().isEmpty()) {

				ruleOrderChars.setPass(false);
				ruleOrderChars.setReason("El documento contiene los siguientes caracteres de orden no permitidos: "
						+ fSlide.getOrderChars().toString());

			}

			return ruleOrderChars;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
