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
package pdm.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pdm.model.Event;

@ApplicationScoped
public class EventRepository {

    @Inject
    private EntityManager em;

    public Event findById(Long id) {
        return em.find(Event.class, id);
    }
    
    public Event findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> event = criteria.from(Event.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(event).where(cb.equal(event.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public Event findByOrganization(long organization) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> event = criteria.from(Event.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(event).where(cb.equal(event.get("organization"), organization));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public Event findByCategory(Long category) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> event = criteria.from(Event.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(event).where(cb.equal(event.get("category"), category));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Event> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> event = criteria.from(Event.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(event).orderBy(cb.asc(event.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
