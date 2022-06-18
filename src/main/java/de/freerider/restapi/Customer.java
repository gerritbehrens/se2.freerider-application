package de.freerider.restapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.type.TypeReference;


@RestController
class Customer implements ServiceAPI {
	//
	@Autowired
    private ApplicationContext context;
	//
	private final ObjectMapper objectMapper;
	//
	private final HttpServletRequest request;


	/**
	 * Constructor.
	 * 
	 * @param objectMapper entry point to JSON tree for the Jackson library
	 * @param request HTTP request object
	 */
	public Customer(ObjectMapper objectMapper, HttpServletRequest request ) {
		this.objectMapper = objectMapper;
		this.request = request;
	}


	/**
	 * GET /people
	 * 
	 * Return JSON Array of people (compact).
	 * 
	 * @return JSON Array of people
	 */
	@Override
	public ResponseEntity<List<?>> getPeople() {
		//
		ResponseEntity<List<?>> re = null;
		System.err.println( request.getMethod() + " " + request.getRequestURI() );   
		try {
			ArrayNode arrayNode = peopleAsJSON();
			ObjectReader reader = objectMapper.readerFor( new TypeReference<List<ObjectNode>>() { } );
			List<String> list = reader.readValue( arrayNode );
			//
			re = new ResponseEntity<List<?>>( list, HttpStatus.OK );

		} catch( IOException e ) {
			re = new ResponseEntity<List<?>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return re;
	}


	/**
	 * GET /people/pretty
	 * 
	 * Return JSON Array of people (pretty printed with indentation).
	 * 
	 * @return JSON Array of people
	 */
	@Override
	public ResponseEntity<String> getPeoplePretty() {
		//
		ResponseEntity<String> re = null;
		System.err.println( request.getMethod() + " " + request.getRequestURI() );   
		try {
			ArrayNode arrayNode = peopleAsJSON();
			String pretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( arrayNode );
			//
			re = new ResponseEntity<String>( pretty, HttpStatus.OK );

		} catch( IOException e ) {
			re = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return re;
	}


	/**
	 * GET /server/stop
	 * 
	 * Stop sever and shut down application.
	 * @return
	 */
	@Override
	public ResponseEntity<Void> stop() {
		//
 		try {
			System.err.print( request.getRequestURI() + " " + request.getMethod() + "\nshutting down server..." );
			//
			ApplicationContext context = this.context;
			((ConfigurableApplicationContext) context).close();
			//
			System.err.println( "  done." );
			//
            return new ResponseEntity<Void>( HttpStatus.OK );

        } catch( Exception e ) {
            return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR );
        }
	}


	/*
	 * Quick Person class
	 */
	static class Person {
		String firstName = "";
		String lastName = "";
		final List<String> contacts = new ArrayList<String>();

		Person setName( String firstName, String lastName ) {
			this.firstName = firstName;
			this.lastName = lastName;
			return this;
		}

		Person addContact( String contact ) {
			this.contacts.add( contact );
			return this;
		}
	}

	private final Person eric = new Person()
		.setName( "Eric", "Meyer" )
		.addContact( "eric98@yahoo.com" )
		.addContact( "(030) 3945-642298" );
		//
	private final Person anne = new Person()
		.setName( "Anne", "Bayer" )
		.addContact( "anne24@yahoo.de" )
		.addContact( "(030) 3481-23352" );
	//
	private final Person tim = new Person()
		.setName( "Tim", "Schulz-Mueller" )
		.addContact( "tim2346@gmx.de" );

	private final List<Person> people = Arrays.asList( eric, anne, tim );


	private ArrayNode peopleAsJSON() {
		//
		ArrayNode arrayNode = objectMapper.createArrayNode();
		//
		people.forEach( c -> {
			StringBuffer sb = new StringBuffer();
			c.contacts.forEach( contact -> sb.append( sb.length()==0? "" : "; " ).append( contact ) );
			arrayNode.add(
				objectMapper.createObjectNode()
					.put( "name", c.lastName )
					.put( "first", c.firstName )
					.put( "contacts", sb.toString() )
			);
		});
		return arrayNode;
	}
}
