package com.analyzer.html.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.analyzer.html.provider.PropertyValuesProvider;
import com.analyzer.html.provider.CreateConnectionProvider;
import com.analyzer.html.provider.DecodeHtmlProvider;
import com.analyzer.html.vo.Slide;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * Controlador del documento
 * 
 * @author Raúl
 *
 */
@Controller
public class DocumentController {

	private static Logger logger = Logger.getLogger(DocumentController.class);

	/**
	 * Carga del fichero properties
	 * 
	 * @return fichero analyzer.properties
	 * @throws IOException
	 * 
	 */
	private Properties loadProperties() throws IOException {

		// Cargamos el fichero properties
		PropertyValuesProvider properties = new PropertyValuesProvider();
		return properties.getPropertiesValues("analyzer.properties");
	}

	/**
	 * Permite añadir un nuevo documento a la bbdd
	 * 
	 * @param s: Cuerpo de la petición
	 * @param r: Petición
	 * @return: Respuesta HTTP
	 * @throws IOException
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/analyzer/api/documents")
	@ResponseBody
	public ResponseEntity<String> createDocument(@RequestBody Slide s, HttpServletRequest r) throws IOException {

		logger.info("Registro de la diapositiva en bbdd");

		Properties p = loadProperties();

		// Validamos el contenido de la diapositiva
		if (StringUtils.isNotBlank(s.getContent())) {

			// Nos conectamos a la bbdd y utilizamos la colección (definidas en
			// analyzer.properties)
			DBCollection collection = CreateConnectionProvider.createConnecion();

			if (collection != null) {

				// Decodificamos el html:
				String htmlDecoded = DecodeHtmlProvider.decode(s.getContent());

				if (htmlDecoded != null) {

					try {

						// Se construye un ObjectId mediante la fecha para identificar
						// al documento en la colección, de manera que podamos devolver dicho
						// identificador en la cabecera de la respuesta CREATED (201)
						Date now = new Date();
						ObjectId documentID = new ObjectId(now);

						BasicDBObject doc = new BasicDBObject();
						doc.put("_id", documentID);
						doc.put("registrationDate", now);
						doc.put("content", htmlDecoded);
						collection.insert(doc);

						// Se construye la cabecera de la respuesta:
						HttpHeaders headers = new HttpHeaders();
						headers.add("Content-Type", "application/json;charset=UTF-8");
						headers.add("Location", r.getRequestURL().toString().concat("/" + documentID.toString()));

						logger.info("El documento se ha registrado correctamente");
						return new ResponseEntity<>(headers, HttpStatus.CREATED);
					}

					catch (Exception e) {
						logger.error("No se ha podido registrar el documento html en la bbdd");
						return new ResponseEntity<>(p.getProperty("cmu-errors.registrationError"),
								HttpStatus.INTERNAL_SERVER_ERROR);
					}

				} else {
					logger.error("No se ha podido decodificar el documento html");
					return new ResponseEntity<>(p.getProperty("cmu-errors.decodeError"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				logger.error("No se ha podido conectar con la base de datos");
				return new ResponseEntity<>(p.getProperty("cmu-errors.connectionError"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			logger.error("No se ha podido recuperar el contenido de la diapositiva");
			return new ResponseEntity<>(p.getProperty("cmu-errors.contentEmpty"), HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Permite eliminar un documento de la bbdd
	 * 
	 * @param documentId: Identificador del documento a eliminar
	 * @return: Respuesta HTTP
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/analyzer/api/documents/{document_id}")
	@ResponseBody
	public ResponseEntity<String> deleteDocument(@PathVariable("document_id") String documentId) throws IOException {

		logger.info("Eliminación de diapositiva en bbdd");

		Properties p = loadProperties();

		// Nos conectamos a la bbdd y utilizamos la colección (definidas en
		// analyzer.properties)
		DBCollection collection = CreateConnectionProvider.createConnecion();

		if (collection != null) {

			ObjectId docID = new ObjectId(documentId);
			BasicDBObject d = new BasicDBObject();
			d.put("_id", docID);

			if (collection.getCount(d) == 1) {

				logger.info("Se ha encontrado el recurso");

				collection.remove(d);

				if (collection.getCount(d) == 0) {

					logger.error("Se ha eliminado el documento correctamente");
					return new ResponseEntity<>(p.getProperty("cmu-errors.docDeleted"), HttpStatus.OK);

				} else {
					logger.error("No se ha eliminado el documento");
					return new ResponseEntity<>(p.getProperty("cmu-errors.docNotDeleted"),
							HttpStatus.INTERNAL_SERVER_ERROR);

				}

			} else {

				logger.error("No se ha encontrado el documento");
				return new ResponseEntity<>(p.getProperty("cmu-errors.docNotFound"), HttpStatus.NOT_FOUND);

			}

		} else {
			logger.error("No se ha podido conectar con la base de datos");
			return new ResponseEntity<>(p.getProperty("cmu-errors.connectionError"), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
