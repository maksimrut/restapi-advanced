package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code CertificateQueryCreator} designed to create a query to database.
 *
 * @author Maksim Rutkouski
 */
public class CertificateQueryCreator {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";
    private static final String DESC = "desc";
    private static final String CREATE_DATE = "createDate";

    public static CriteriaQuery<GiftCertificate> createQuery(CertificateQueryParameters queryParameters,
                                                      CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.addAll(addPartNamePredicate(queryParameters, criteriaBuilder, certificateRoot));
        predicates.addAll(addTagNamesPredicates(queryParameters, criteriaBuilder, certificateRoot));

        criteriaQuery.select(certificateRoot).where(predicates.toArray(new Predicate[0]));
        addSorting(queryParameters, criteriaBuilder, certificateRoot, criteriaQuery);
        return criteriaQuery;
    }

    private static List<Predicate> addPartNamePredicate(CertificateQueryParameters queryParameters,
                                                        CriteriaBuilder criteriaBuilder,
                                                        Root<GiftCertificate> certificateRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (queryParameters.getPartName() != null) {
            Predicate containsInCertificateName = criteriaBuilder.like(certificateRoot.get(NAME), "%" + queryParameters.getPartName() + "%");
            Predicate containsInCertificateDescription = criteriaBuilder.like(certificateRoot.get(DESCRIPTION), "%" + queryParameters.getPartName() + "%");
            predicates.add(criteriaBuilder.or(containsInCertificateName, containsInCertificateDescription));
        }
        return predicates;
    }

    private static List<Predicate> addTagNamesPredicates(CertificateQueryParameters queryParameters,
                                                         CriteriaBuilder criteriaBuilder,
                                                         Root<GiftCertificate> certificateRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (queryParameters.getTagNames() != null) {
            predicates = Arrays.stream(queryParameters.getTagNames())
                    .map(tagName -> criteriaBuilder.equal(certificateRoot.join(TAGS).get(NAME), tagName))
                    .collect(Collectors.toList());
        }
        return predicates;
    }

    private static void addSorting(CertificateQueryParameters queryParameters, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> certificateRoot, CriteriaQuery<GiftCertificate> criteriaQuery) {
        List<Order> orders = new ArrayList<>();
        if (queryParameters.getSortByName() != null) {
            if (queryParameters.getSortByName().equals(DESC)) {
                orders.add(criteriaBuilder.desc(certificateRoot.get(NAME)));
            } else {
                orders.add(criteriaBuilder.asc(certificateRoot.get(NAME)));
            }
        }
        if (queryParameters.getSortByDate() != null) {
            if (queryParameters.getSortByDate().equals(DESC)) {
                orders.add(criteriaBuilder.desc(certificateRoot.get(CREATE_DATE)));
            } else {
                orders.add(criteriaBuilder.asc(certificateRoot.get(CREATE_DATE)));
            }
        }
        criteriaQuery.orderBy(orders);
    }
}
