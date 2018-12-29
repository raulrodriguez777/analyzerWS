package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla colorText: El color del texto debe ser negro
 * 
 * @author Raúl
 *
 */
public class RuleColorText extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleColorText.class);
	private static final String DEFINITIONRULE = "rules.RuleColorText";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateColorText(Slide fSlide) throws IOException {

		logger.info("Consulta regla color del texto...");

		Rule ruleColorText = allOK(DEFINITIONRULE);

		if (ruleColorText != null) {

			if (!fSlide.getColors().isEmpty()) {

				List<String> notAcceptedColors = new ArrayList<>();

				for (String color : fSlide.getColors()) {

					if (!color.equals("#000000") && !color.equalsIgnoreCase("black")
							&& !color.equalsIgnoreCase("#Hex_RGB") && !color.equalsIgnoreCase("RGB(0, 0, 0)")
							&& !color.equalsIgnoreCase("RGB(0,0,0)") && !color.equalsIgnoreCase("hsl(0, 0%, 0%)")
							&& !color.equalsIgnoreCase("hsl(0,0%,0%)")) {

						notAcceptedColors.add(color);

					}
				}

				if (!notAcceptedColors.isEmpty()) {

					ruleColorText.setPass(false);
					ruleColorText.setReason("El documento presenta los siguientes colores de texto no permitidos: "
							+ notAcceptedColors.toString());

				}

			}

			return ruleColorText;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
