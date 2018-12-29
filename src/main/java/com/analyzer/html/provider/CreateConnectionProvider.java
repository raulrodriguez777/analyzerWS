package com.analyzer.html.provider;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class CreateConnectionProvider {

	private CreateConnectionProvider() {}
	
	private static Logger logger = Logger.getLogger(CreateConnectionProvider.class);

	public static DBCollection createConnecion() throws IOException {

		PropertyValuesProvider properties = new PropertyValuesProvider();
		Properties p = properties.getPropertiesValues("analyzer.properties");
		
		String bbdd = p.getProperty("db.datasource.dbname");
		String collection = p.getProperty("db.datasource.collecionName");

		logger.info("Conectando a la coleccion ".concat(collection).concat(" de la bbdd ").concat(bbdd));
		try {

			// PASO 1: Conexión al Server de MongoDB Pasandole el host y el puerto
			MongoClient mongoClient = new MongoClient(p.getProperty("db.datasource.hostName"),
					Integer.parseInt(p.getProperty("db.datasource.port")));

			// PASO 2: Conexión a la base de datos pasada en el primer parametro
			DB db = mongoClient.getDB(bbdd);

			// PASO 3: Obtenemos una coleccion para trabajar con ella
			// Si no existe la colección, se crea automáticamente

			return db.getCollection(collection);

		} catch (Exception e) {
			logger.error("Se ha producido un error al conectarse con la bbdd", e);
			return null;
		}

	}

}
