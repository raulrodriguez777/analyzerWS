package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Slide;

/**
 * Regla colorBackground: El color del fondo debe ser blanco sólido
 * 
 * @author Raúl
 *
 */
public class RuleColorBackground extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleColorBackground.class);
	private static final String DEFINITIONRULE = "rules.RuleColorBackground";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateColorBackground(Slide fSlide) throws IOException {

		logger.info("Consulta regla color del fondo...");

		Rule ruleColorBackground = allOK(DEFINITIONRULE);

		if (ruleColorBackground != null) {

			if (!fSlide.getBackgroundColors().isEmpty()) {

				List<String> notAcceptedColors = new ArrayList<>();

				for (String color : fSlide.getBackgroundColors()) {

					if (!color.equalsIgnoreCase("#FFFFFF") && !color.equalsIgnoreCase("white")
							&& !color.equalsIgnoreCase("hsl(0, 0%, 100%)") && !color.equalsIgnoreCase("hsl(0,0%,100%)")
							&& !color.equalsIgnoreCase("RGB(255, 255, 255)")
							&& !color.equalsIgnoreCase("RGB(255,255,255)")) {

						notAcceptedColors.add(color);

					}
				}

				if (!notAcceptedColors.isEmpty()) {

					ruleColorBackground.setPass(false);
					ruleColorBackground
							.setReason("El documento presenta los siguientes colores de fondo no permitidos: "
									+ notAcceptedColors.toString());

				}

			}

			return ruleColorBackground;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
