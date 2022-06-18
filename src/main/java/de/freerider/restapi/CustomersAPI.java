package de.freerider.restapi;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface CustomersAPI {
    /**
     * GET /customers
     *
     * @return JSON Array with customers (compact).
     */

    /*
     * Swagger API doc annotations:
     */
    @Operation(
            summary = "Return all customers from repository.",		// appears as text in API short-list
            description = "Return all customers from repository.",	// appears as text inside API
            tags={ "customers-controller" }	// appears in swagger-ui URL: http://localhost:8080/swagger-ui/index.html#/customers-controller
    )
    @ApiResponses( value = {	// also auto-derived by Swagger
            @ApiResponse( responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @ApiResponse( responseCode = "401", description = "Unauthorized"),
            @ApiResponse( responseCode = "403", description = "Forbidden"),
            // to remove "404" from docs, set SwaggerConfig::Docket.useDefaultResponseMessages(true) // ->false
//		@ApiResponse( responseCode = "404", description = "Not Found")
    })

    /*
     * Spring REST Controller annotation:
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/customers",	// relative to interface @RequestMapping
            produces = { "application/json" }
    )
    //
    ResponseEntity<List<?>> getCustomers();


    /**
     * GET /customers/{id}
     *
     * @return JSON Array with customers (compact).
     */

    /*
     * Swagger API doc annotations:
     */
    @Operation(
            summary = "Return customer with {id} from repository.",
            description = "Return customer with {id} from repository.",
            tags={ "customers-controller" }
    )

    /*
     * Spring REST Controller annotation:
     */
    @RequestMapping(
            method=RequestMethod.GET,
            value="api/v1/customers/{id}",	// relative to interface @RequestMapping
            produces={ "application/json" }
    )
    //
    ResponseEntity<?> getCustomer(
            @PathVariable("id")
            @ApiParam(value = "Customer id", required = true)
            long id
    );


    /**
     * POST /customers
     *
     * Add new customers from JSON data passed as array of JSON objects in the request body.
     * Multiple customers can be posted with multiple JSON objects from the same request.
     * Id's are assigned, if id-attributes are missing or are empty in JSON data.
     *
     * JSON data containing id of objects that are already present are rejected. Rejected
     * objects are returned in the response with error 409 (conflict).
     *
     * Status 201 (created) is returned with empty array of conflicts when all objects were
     * accepted. Partial acceptance of objects from the request is possible, but error 409 is
     * returned with the array of rejected objects.
     *
     * @param jsonMap array of maps with raw JSON {@code <key,obj>}-data.
     * @return JSON array with the rejected JSON objects, empty array [] if all objects were accepted.
     */

    /*
     * Swagger API doc annotations:
     */
    @Operation(
            summary = "Add new customers to repository.",
            description = "Add new customers to repository.",
            tags={ "customers-controller" }
    )

    /*
     * Spring REST Controller annotation:
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "api/v1/customers"	// relative to interface @RequestMapping
    )
    //
    ResponseEntity<List<?>> postCustomers( @RequestBody Map<String, Object>[] jsonMap );


    /**
     * PUT /customers
     *
     * Update existing customers from JSON data passed as array of JSON objects in the request body.
     * Multiple customers can be updated from multiple JSON objects from the same request.
     *
     * JSON data missing id or with id that are not found are rejected. Rejected JSON objects
     * are returned in the response with error 404 (not found).
     *
     * Status 202 (accepted) is returned with empty array of conflicts when all updates could be
     * performed. Partial acceptance of updates is possible for entire objects only (not single
     * attributes). Error 409 (conflict) is returned for errors other than an object (id) was not
     * found along with the array of rejected objects.
     *
     * @param jsonMap array of maps with raw JSON {@code <key,obj>}-data.
     * @return JSON array with the rejected JSON objects, empty array [] if all updates were accepted.
     */

    /*
     * Swagger API doc annotations:
     */
    @Operation(
            summary = "Update existing customers in repository.",
            description = "Update existing customers in repository.",
            tags={ "customers-controller" }
    )

    /*
     * Spring REST Controller annotation:
     */
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "api/v1/customers"	// relative to interface @RequestMapping
    )
    //
    ResponseEntity<List<?>> putCustomers( @RequestBody Map<String, Object>[] jsonMap );


    /**
     * DELETE /customers/{id}
     *
     * Delete existing customer by its id. A missing id or an id that was not found
     * returns error 404 (not found).
     *
     * Status 202 (accepted) is returned with successful completion of the operation.
     *
     * @param id id of object to delete.
     * @return status code: 202 (accepted), 404 (not found).
     */

    /*
     * Swagger API doc annotations:
     */
    @Operation(
            summary = "Delete customer by its id from repository.",
            description = "Delete customer by its id from repository.",
            tags={ "customers-controller" }
    )

    /*
     * Spring REST Controller annotation:
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "api/v1/customers/{id}"	// relative to interface @RequestMapping
    )
    //
    ResponseEntity<?> deleteCustomer( @PathVariable("id") Long id );


    /*
     * Alternative using DTO serialization.
     */
//	@RequestMapping( method = RequestMethod.POST, value = "" )
//	public ResponseEntity<List<?>> postCustomers( @RequestBody CustomerJsonDTO[] dtos );

}

