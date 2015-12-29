/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pdm.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pdm.data.EventRepository;
import pdm.model.Event;
import pdm.service.EventModify;
import pdm.service.EventRegistration;
import pdm.service.EventRemove;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/events")
@RequestScoped
@Stateful
public class EventService {
	
	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private EventRepository repository;

	@Inject
	EventRegistration registration;
	
	@Inject
    EventRemove remove;
	
	@Inject
    EventModify modify;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> listAllEvents() {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Event lookupEventById(@PathParam("id") long id) {
    	Event event = repository.findById(id);
        if (event == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return event;
    }
    
    @GET
    @Path("/organization/{organization:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Event lookupEventByOrganization(@PathParam("organization") long organization) {
    	Event event = repository.findByOrganization(organization);
        if (event == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return event;
    }
    
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeEvent(Event event) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates member using bean validation
        	Event eventR = repository.findByName(event.getName());
        	validateEventRemove(eventR);

            remove.remove(eventR);

            // Create an "ok" response
            builder = Response.ok().entity(eventR);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("name", e.getMessage());
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
    
    private void validateEventRemove(Event event) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (!(nameAlreadyExists(event.getName()))) {
            throw new ValidationException("Unique Name Violation");
        }
    }
    
    @POST
    @Path("/modify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyEvent(Event event) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates member using bean validation
        	//Event eventR = repository.findById(event.getId());
        	//validateEventRemove(eventR);

        	modify.modify(event);

            // Create an "ok" response
        	Map<String, String> responseObj = new HashMap<>();
            responseObj.put("response", "Modification réussie");
            builder = Response.ok().entity(responseObj);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("name", e.getMessage());
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
    
    @GET
    @Path("/category/{category:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Event lookupEventByCategory(@PathParam("category") long category) {
    	Event event = repository.findByOrganization(category);
        if (event == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return event;
    }
    
    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvent(Event event) {

        Response.ResponseBuilder builder = null;
        try {
            /**
             * 
             * TESSST
             */
        	log.info("Registering event " + event.getName());
        	
        	// Validates member using bean validation
            validateEvent(event);

            registration.register(event);

            // Create an "ok" response
            builder = Response.ok().entity(event);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", e.getMessage());
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param member Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
    private void validateEvent(Event event) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (nameAlreadyExists(event.getName())) {
            throw new ValidationException("Unique Name Violation");
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a member with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean nameAlreadyExists(String name) {
    	Event event = null;
        try {
        	event = repository.findByName(name);
        } catch (NoResultException e) {
            // ignore
        }
        return event != null;
    }
}
