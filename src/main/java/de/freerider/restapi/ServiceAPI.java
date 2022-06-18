package de.freerider.restapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * REST API of simple Service
 * 
 * @author sgra64
 *
 */

public interface ServiceAPI {


	/**
	 * GET /people
	 * 
	 * Return JSON Array of people (compact).
	 * 
	 * @return JSON Array of people
	 */
	@RequestMapping( method = RequestMethod.GET, value = "/people", produces = { "application/json" } )
	ResponseEntity<List<?>> getPeople();


	/**
	 * GET /people/pretty
	 * 
	 * Return JSON Array of people (pretty printed with indentation).
	 * 
	 * @return JSON Array of people
	 */
	@RequestMapping( method = RequestMethod.GET, value = "/people/pretty", produces = { "application/json" } )
	ResponseEntity<String> getPeoplePretty();


	/**
	 * GET /server/stop
	 * 
	 * Stop sever and shut down application.
	 * @return
	 */
	@RequestMapping( method = RequestMethod.GET, value = "/server/stop" )
	ResponseEntity<Void> stop();

}
