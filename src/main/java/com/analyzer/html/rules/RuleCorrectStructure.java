package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;

/**
 * Regla correctStructure: Las oraciones deben tener la estructura: "Sujeto +
 * Verbo + Predicado"
 * 
 * @author Raúl
 *
 */
public class RuleCorrectStructure extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleCorrectStructure.class);
	private static final String DEFINITIONRULE = "rules.RuleCorrectStructure";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateCorrectStructure(Slide fSlide) throws IOException {

		logger.info("Consulta regla estructura de la frase...");

		Rule ruleCorrectStructure = allOK(DEFINITIONRULE);

		if (ruleCorrectStructure != null) {

			List<String> incorrectSentences = new ArrayList<>();
			if (fSlide.getListSentences() != null && !fSlide.getListSentences().isEmpty()) {

				for (Sentence s : fSlide.getListSentences()) {

					if (!s.isCorrectStructure()) {

						incorrectSentences.add(s.getContent());
					}

				}
				if (!incorrectSentences.isEmpty()) {

					ruleCorrectStructure.setPass(false);
					ruleCorrectStructure
							.setReason("El documento tiene las siguientes frases con estructura incorrecta: "
									+ incorrectSentences.toString());
				}

			}

			return ruleCorrectStructure;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
