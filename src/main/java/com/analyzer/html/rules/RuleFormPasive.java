package com.analyzer.html.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.analyzer.html.vo.Rule;
import com.analyzer.html.vo.Sentence;
import com.analyzer.html.vo.Slide;

/**
 * Regla formActive: No se permite el uso de la forma pasiva
 * 
 * @author Raúl
 *
 */
public class RuleFormPasive extends CommonRules {

	private static Logger logger = Logger.getLogger(RuleFormPasive.class);
	private static final String DEFINITIONRULE = "rules.RuleFormPasive";

	/**
	 * Validación de la regla
	 * 
	 * @param fSlide con las características de la diapositiva
	 * @return Regla tras la evaluación
	 * @throws IOException
	 */
	public Rule validateFormPasive(Slide fSlide) throws IOException {

		logger.info("Consulta regla forma pasiva...");

		Rule ruleFormPasive = allOK(DEFINITIONRULE);

		if (ruleFormPasive != null) {

			List<String> incorrectSentences = new ArrayList<>();
			if (fSlide.getListSentences() != null && !fSlide.getListSentences().isEmpty()) {

				for (Sentence s : fSlide.getListSentences()) {

					if (!s.isFormActive()) {

						incorrectSentences.add(s.getContent());
					}

				}
				if (!incorrectSentences.isEmpty()) {

					ruleFormPasive.setPass(false);
					ruleFormPasive.setReason("El documento tiene las siguientes frases en forma pasiva: "
							+ incorrectSentences.toString());
				}

			}

			return ruleFormPasive;

		} else {
			logger.error("La regla no está definida o no está activada en el fichero properties");
			return null;
		}

	}

}
