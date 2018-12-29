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
 * Regla MaxNumer: No puede haber un número mayor a 100.000
 * 
 * @author Raúl
 *
 */
public class RuleMaxNumber extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleMaxNumber.class);
	private static final String DEFINITIONRULE = "rules.RuleMaxNumber";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateMaxNumber(Slide fSlide) throws IOException {

		logger.info("Consulta regla máximo número...");

		Rule ruleMaxNumber = allOK(DEFINITIONRULE);

		if (ruleMaxNumber != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxNumber"))) {

				try {

					int maxNumber = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxNumber"));

					if (!fSlide.getNumbers().isEmpty()) {

						List<Double> numberNotAccepted = new ArrayList<>();

						for (Double number : fSlide.getNumbers()) {

							if (number > Double.valueOf(maxNumber)) {

								numberNotAccepted.add(number);

							}

						}

						if (!numberNotAccepted.isEmpty()) {

							ruleMaxNumber.setPass(false);
							ruleMaxNumber.setReason("El documento contiene los siguientes números no permitidos: "
									+ numberNotAccepted.toString());

						}

					}

					return ruleMaxNumber;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el número máximo permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo permitido");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
