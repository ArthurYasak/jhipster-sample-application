package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TestEntityRepositoryWithBagRelationshipsImpl implements TestEntityRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TestEntity> fetchBagRelationships(Optional<TestEntity> testEntity) {
        return testEntity.map(this::fetchTestUsers);
    }

    @Override
    public Page<TestEntity> fetchBagRelationships(Page<TestEntity> testEntities) {
        return new PageImpl<>(
            fetchBagRelationships(testEntities.getContent()),
            testEntities.getPageable(),
            testEntities.getTotalElements()
        );
    }

    @Override
    public List<TestEntity> fetchBagRelationships(List<TestEntity> testEntities) {
        return Optional.of(testEntities).map(this::fetchTestUsers).orElse(Collections.emptyList());
    }

    TestEntity fetchTestUsers(TestEntity result) {
        return entityManager
            .createQuery(
                "select testEntity from TestEntity testEntity left join fetch testEntity.testUsers where testEntity.id = :id",
                TestEntity.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<TestEntity> fetchTestUsers(List<TestEntity> testEntities) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, testEntities.size()).forEach(index -> order.put(testEntities.get(index).getId(), index));
        List<TestEntity> result = entityManager
            .createQuery(
                "select testEntity from TestEntity testEntity left join fetch testEntity.testUsers where testEntity in :testEntities",
                TestEntity.class
            )
            .setParameter("testEntities", testEntities)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
