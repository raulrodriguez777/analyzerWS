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
 * Regla Font: La fuente del texto tiene que pertenecer a los estilos aceptados
 * 
 * @author Raúl
 *
 */
public class RuleFontText extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleFontText.class);
	private static final String DEFINITIONRULE = "rules.RuleFontText";

	/**
	 * Método para recuperar la lista de fuentes definidas en el fichero properties
	 * 
	 * @return La lista de fuentes definidas
	 * @throws IOException
	 */
	private List<String> getAcceptedFonts() throws IOException {

		logger.info("Consulta las fuentes definidas en el fichero rule-definition.properties...");

		List<String> acceptedFont = new ArrayList<>();

		// Carga del fichero properties
		Properties pRules = loadProperties("rules-definition.properties");
		int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

		if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".AllowedFonts"))) {

			String[] fonts = pRules.getProperty("rules.r" + ruleId + ".AllowedFonts").split(",");

			for (int i = 0; i < fonts.length; i++) {

				if (!acceptedFont.contains(fonts[i].toUpperCase().trim())) {

					acceptedFont.add(fonts[i].toUpperCase().trim());

				}

			}

		}

		return acceptedFont;
	}

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateFontText(Slide fSlide) throws IOException {

		logger.info("Consulta regla Fuente del texto...");

		Rule ruleFont = allOK(DEFINITIONRULE);

		if (ruleFont != null) {

			List<String> fontsAllowed = getAcceptedFonts();

			if (!fSlide.getFonts().isEmpty()) {

				List<String> fontNotAccepted = new ArrayList<>();
				for (String font : fSlide.getFonts()) {

					if (!fontsAllowed.contains(font.toUpperCase())) {

						fontNotAccepted.add(font);
					}

				}
				if (!fontNotAccepted.isEmpty()) {

					ruleFont.setPass(false);
					ruleFont.setReason("El documento contiene las siguientes fuentes de texto no permitidas: "
							+ fontNotAccepted.toString());
				}
			}

			return ruleFont;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
