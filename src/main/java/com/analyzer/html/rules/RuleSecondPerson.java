package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;

/**
 * Regla secondPerson: Solo se permite el uso de la segunda persona
 * 
 * @author Raúl
 *
 */
public class RuleSecondPerson extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleSecondPerson.class);
	private static final String DEFINITIONRULE = "rules.RuleSecondPerson";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateSecondPerson(Slide fSlide) throws IOException {

		logger.info("Consulta regla segunda persona...");

		Rule ruleSecondPerson = allOK(DEFINITIONRULE);

		if (ruleSecondPerson != null) {

			List<String> incorrectSentences = new ArrayList<>();
			if (fSlide.getListSentences() != null && !fSlide.getListSentences().isEmpty()) {

				for (Sentence s : fSlide.getListSentences()) {

					if (!s.isSecondPerson()) {

						incorrectSentences.add(s.getContent());
					}

				}
				if (!incorrectSentences.isEmpty()) {

					ruleSecondPerson.setPass(false);
					ruleSecondPerson
							.setReason("El documento tiene las siguientes frases que no están en segunda persona: "
									+ incorrectSentences.toString());
				}

			}

			return ruleSecondPerson;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
