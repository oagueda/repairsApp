package xyz.oagueda.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import tech.jhipster.service.QueryService;

public abstract class SpecQueryService<T> extends QueryService<T> {

    Specification<T> createLikeFilter(String filter, List<SingularAttribute<T, ?>> attributes) {
        String[] busquedaListado = (null != filter && !filter.trim().isEmpty()) ? filter.split(" ") : new String[0];

        List<Specification<T>> specifications = new ArrayList<>();
        for (String searchParam : busquedaListado) {
            Specification<T> combinedSpec = Specification.where(null);

            for (SingularAttribute<T, ?> attribute : attributes) {
                combinedSpec = combinedSpec.or(buildCustomLikeSpecification(searchParam, attribute));
            }

            specifications.add(combinedSpec);
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    Specification<T> createLikeFilter(String filter, List<SingularAttribute<T, ?>> attributes, Map<String, String> referrAttributes) {
        String[] busquedaListado = (null != filter && !filter.trim().isEmpty()) ? filter.split(" ") : new String[0];

        List<Specification<T>> specifications = new ArrayList<>();
        for (String searchParam : busquedaListado) {
            Specification<T> combinedSpec = Specification.where(null);

            for (SingularAttribute<T, ?> attribute : attributes) {
                combinedSpec = combinedSpec.or(buildCustomLikeSpecification(searchParam, attribute));
            }

            for (Map.Entry<String, String> valueField : referrAttributes.entrySet()) {
                combinedSpec = combinedSpec.or(
                    buildCustomReferringEntitySpecification(searchParam, valueField.getKey(), valueField.getValue())
                );
            }

            specifications.add(combinedSpec);
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    Specification<T> buildCustomReferringEntitySpecification(String searchParam, String reference, String valueField) {
        return (root, query, cb) -> {
            Expression<String> stringValue = cb.literal("%" + searchParam + "%");

            String[] fields = valueField.split(",");

            // Obtain the SingularAttribute for the valueField
            EntityType<?> entityType = root.getModel();
            SingularAttribute<?, ?> valueAttribute =
                ((EntityType<?>) entityType.getSingularAttribute(reference).getType()).getSingularAttribute(fields[0]);

            Join<?, ?> joined = root.join(reference, JoinType.LEFT);

            Predicate predicate = cb.like(buildExpression(joined.get(fields[0]), valueAttribute, cb), cb.lower(stringValue));

            for (int i = 1; i < fields.length; i++) {
                SingularAttribute<?, ?> valueForAttribute =
                    ((EntityType<?>) entityType.getSingularAttribute(reference).getType()).getSingularAttribute(fields[i]);

                predicate = cb.or(predicate, cb.like(buildExpression(joined.get(fields[i]), valueForAttribute, cb), cb.lower(stringValue)));
            }

            return predicate;
        };
    }

    Specification<T> buildCustomLikeSpecification(String searchParam, SingularAttribute<T, ?> field) {
        return (root, query, cb) -> {
            Expression<String> stringValue = cb.literal("%" + searchParam + "%");
            return cb.like(buildExpression(root.get(field), field, cb), cb.lower(stringValue));
        };
    }

    Expression<String> buildExpression(Path<?> expression, SingularAttribute<?, ?> field, CriteriaBuilder cb) {
        Class<?> attributeType = field.getJavaType();
        if (
            Date.class.isAssignableFrom(attributeType) ||
            LocalDate.class.isAssignableFrom(attributeType) ||
            Instant.class.isAssignableFrom(attributeType)
        ) {
            return cb.function("DATE_FORMAT", String.class, expression, cb.literal("%d/%m/%Y %r"));
        }

        return cb.lower(expression.as(String.class));
    }

    Map<String, String> createAttributeMap(String... attributes) {
        if (attributes.length % 2 != 0) {
            throw new IllegalArgumentException("The attribute must be provided in pairs.");
        }

        Map<String, String> attributeMap = new HashMap<>();
        for (int i = 0; i < attributes.length; i += 2) {
            String key = attributes[i];
            String value = attributes[i + 1];
            attributeMap.put(key, value);
        }

        return attributeMap;
    }
}
