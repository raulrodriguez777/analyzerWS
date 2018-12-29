package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;

/**
 * Regla hasSubject: Es obligatorio el uso de sujeto
 * 
 * @author Raúl
 *
 */
public class RuleHasSubject extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleHasSubject.class);
	private static final String DEFINITIONRULE = "rules.RuleHasSubject";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateHasSubject(Slide fSlide) throws IOException {

		logger.info("Consulta regla tiene sujeto...");

		Rule ruleHasSubject = allOK(DEFINITIONRULE);

		if (ruleHasSubject != null) {

			List<String> incorrectSentences = new ArrayList<>();
			if (fSlide.getListSentences() != null && !fSlide.getListSentences().isEmpty()) {

				for (Sentence s : fSlide.getListSentences()) {

					if (!s.isHaveSubject()) {

						incorrectSentences.add(s.getContent());
					}

				}
				if (!incorrectSentences.isEmpty()) {

					ruleHasSubject.setPass(false);
					ruleHasSubject.setReason(
							"El documento tiene las siguientes frases sin sujeto: " + incorrectSentences.toString());
				}

			}

			return ruleHasSubject;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
