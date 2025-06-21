package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Category;
import com.example.eCommerce.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    public Page<Product> search(String name, String category, Double price, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // builder per i criteri

        //query per il contenuto
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        // join con categoria
        Join<Product, Category> categoryJoin =product.join("category", JoinType.LEFT);


        // Query per il conteggio totale (necessaria per Page)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);


        List<Predicate> predicates = new ArrayList<>();



        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(product.get("name"), "%" + name + "%"));
        }

        //campo di un altra tabella
        if (category != null && !category.isEmpty()) {
            predicates.add(cb.like(categoryJoin.get("name"), "%" + category + "%"));
        }

        if (price != null) {
            predicates.add(cb.ge(product.get("price"), price));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // prendo i risultati
        TypedQuery<Product> query = entityManager.createQuery(cq);
        //applico la paginazione
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults((int) pageable.getPageSize());

        // eseguo la query
        List<Product> content = query.getResultList();

        //conto quanti risultati ci sono
       countQuery.select(cb.count(countRoot));
       Long total= entityManager.createQuery(countQuery).getSingleResult();

        return  new PageImpl<>(content, pageable, total);
    }
}
