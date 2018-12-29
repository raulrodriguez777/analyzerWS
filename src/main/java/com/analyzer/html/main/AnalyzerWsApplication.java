package com.analyzer.html.main;


import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.analyzer.html" })
public class AnalyzerWsApplication {

	/**
	 * Método estándar que sigue la convención de Java para un punto de entrada de
	 * la aplicación. Se delega entonces en el método run de la clase
	 * SpringApplication que pone en marcha nuestra aplicación usando spring y a
	 * continuación arranca un servidor web incorporado.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		SpringApplication.run(AnalyzerWsApplication.class, args);


	}
}
