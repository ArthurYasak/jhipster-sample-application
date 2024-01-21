package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TestEntityRepositoryWithBagRelationships {
    Optional<TestEntity> fetchBagRelationships(Optional<TestEntity> testEntity);

    List<TestEntity> fetchBagRelationships(List<TestEntity> testEntities);

    Page<TestEntity> fetchBagRelationships(Page<TestEntity> testEntities);
}
