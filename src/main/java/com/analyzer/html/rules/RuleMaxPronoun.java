package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;

/**
 * Regla maxPronoun: No se permite tener más de 2 pronombres por frase
 * 
 * @author Raúl
 *
 */
public class RuleMaxPronoun extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleMaxPronoun.class);
	private static final String DEFINITIONRULE = "rules.RuleMaxPronoun";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateMaxPronoun(Slide fSlide) throws IOException {

		logger.info("Consulta regla máximo número de pronombres...");

		Rule ruleMaxPronoun = allOK(DEFINITIONRULE);

		if (ruleMaxPronoun != null) {

			// Carga del fichero properties
			Properties pRules = loadProperties("rules-definition.properties");
			int ruleId = Integer.parseInt(pRules.getProperty(DEFINITIONRULE).split("-")[0]);

			if (StringUtils.isNotBlank(pRules.getProperty("rules.r" + ruleId + ".maxPronoun"))) {

				try {

					int maxPronoun = Integer.parseInt(pRules.getProperty("rules.r" + ruleId + ".maxPronoun"));

					List<String> incorrectSentences = new ArrayList<>();
					if (fSlide.getListSentences() != null && !fSlide.getListSentences().isEmpty()) {

						for (Sentence s : fSlide.getListSentences()) {

							if (s.getNumPronoun() > maxPronoun) {

								incorrectSentences.add(s.getContent() + ": " + s.getNumPronoun() + " pronombres");
							}

						}
						if (!incorrectSentences.isEmpty()) {

							ruleMaxPronoun.setPass(false);
							ruleMaxPronoun.setReason(
									"El documento tiene las siguientes frases que superan el número de pronombres: "
											+ incorrectSentences.toString());
						}

					}

					return ruleMaxPronoun;

				} catch (Exception e) {
					logger.error("Se ha producido un error al parsear el límite de pronombres permitido");
					return null;
				}

			} else {
				logger.error("No está definido el número máximo de pronombres permitidos");
				return null;

			}

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
