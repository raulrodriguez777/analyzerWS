package com.analyzer.html.rules;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla formatDate: El Formato de la fecha debe ser DD de mes del YYYY
 * 
 * @author Raúl
 *
 */
public class RuleDateFormat extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleDateFormat.class);
	private static final String DEFINITIONRULE = "rules.RuleDateFormat";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateDateFormat(Slide fSlide) throws IOException {

		logger.info("Consulta regla Fotmato Fechas...");

		Rule ruleDateFormat = allOK(DEFINITIONRULE);

		if (ruleDateFormat != null) {

			if (!fSlide.getWrongDateFormat().isEmpty()) {

				ruleDateFormat.setPass(false);
				ruleDateFormat.setReason("El documento contiene las siguientes fechas con formato incorrecto: "
						+ fSlide.getWrongDateFormat().toString());

			}

			return ruleDateFormat;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
