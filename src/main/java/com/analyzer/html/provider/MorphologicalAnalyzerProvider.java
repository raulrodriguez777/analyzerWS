package com.analyzer.html.provider;

import org.apache.log4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.analyzer.html.vo.Data;
import com.google.gson.Gson;

public class MorphologicalAnalyzerProvider {

	private MorphologicalAnalyzerProvider() {
	}

	private static Logger logger = Logger.getLogger(MorphologicalAnalyzerProvider.class);

	public static Data morphologicalAnalyzer(String text) {

		logger.info("Conectando con la api...");

		try {

			RestTemplate rest = new RestTemplate();

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

			final String uri = "https://api.meaningcloud.com/parser-2.0";
			map.add("key", "21616db5e4515aff6da177a89beec3c8");
			map.add("of", "json");
			map.add("lang", "es");
			map.add("verbose", "y");
			map.add("txt", text);
			map.add("uw", "y");
			map.add("ilang", "en");

			String result = rest.postForObject(uri, map, String.class);

			return new Gson().fromJson(result, Data.class);

		} catch (Exception e) {

			logger.error("No ha sido posible conectar con la api", e);
			return null;
		}

	}
}
