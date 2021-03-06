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
package pdm.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import pdm.model.Organization;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class OrganizationRemove {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Organization> eventEventSrc;

    public void remove(Organization organization) throws Exception {
        log.info("Removing organization " + organization.getName());
        em.remove(organization);
        eventEventSrc.fire(organization);
    }
}
