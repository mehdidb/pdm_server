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

import pdm.model.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class CategoryRepository {

    @Inject
    private EntityManager em;

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public Category findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> criteria = cb.createQuery(Category.class);
        Root<Category> category = criteria.from(Category.class);
        criteria.select(category).where(cb.equal(category.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Category> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> criteria = cb.createQuery(Category.class);
        Root<Category> category = criteria.from(Category.class);
        criteria.select(category).orderBy(cb.asc(category.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
