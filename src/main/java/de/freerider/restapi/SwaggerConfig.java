package de.freerider.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;


@Configuration
@EnableWebMvc
@PropertySource( "classpath:swagger.properties" )
@ConfigurationProperties( prefix="app.api" )//, ignoreInvalidFields=true, ignoreUnknownFields=false )
public class SwaggerConfig {

	/*
	 * property file attributes, @Value( "${basepackage}" )
	 */
	private String version;

	private String title;

	private String description;

	private String basePackage;

	private String contactName;

	private String contactEmail;

	@Autowired
	private Environment env;


	@Bean
	public Docket api() {
		//
		//app.api.endpoints.customers
		String customersEP_URL = regexEP_URL( "app.api.endpoints.customers", "/api/v1/customers" );
		String serverEP_URL = regexEP_URL( "app.api.endpoints.server", "/server" );
		//
		return new Docket(
				// show http://localhost:8080/v2/api-docs in UI for swagger: '2.0' api-doc
//				DocumentationType.SWAGGER_2
				//
				// show http://localhost:8080/v3/api-docs in UI for open api: '3.0.3' api-doc
				DocumentationType.OAS_30
			)
			//
			.useDefaultResponseMessages( true ) // disable Auto-generation of Status codes
			//
			.select()
			// select basePackage for REST-endpoint documentation
			
			.apis( RequestHandlerSelectors.basePackage( "de.freerider.restapi" ) )
//			.paths( PathSelectors.any() )
//			.paths( PathSelectors.regex( "/api/v1/customers.*" ) )
			.paths( PathSelectors.regex( customersEP_URL + "|" + serverEP_URL ) )
//			.paths( PathSelectors.regex( customersEP_URL + "|" + serverEP_URL + "|/people.*" ) )
			.build()
//			.directModelSubstitute( LocalDate.class, java.sql.Date.class )
//			.directModelSubstitute( LocalDateTime.class, java.util.Date.class )
			.apiInfo( apiInfo() );
	}

	private ApiInfo apiInfo() {
		//
		return new ApiInfoBuilder()
			.title( title )
			.description( description )
			.version( version )
			.contact( new Contact( contactName, null, contactEmail ) )
			.build();
	}


	/*
	 * Getters/setters are needed for reading properties.
	 */

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}


	/*
	 * private methods
	 */
	private String regexEP_URL( String propKey, String valueIfNoKey ) {
		String rep = env.getProperty( propKey );
		rep = rep != null? rep : valueIfNoKey;
		return rep + ".*";	// append ".*" for regex
	}

}
