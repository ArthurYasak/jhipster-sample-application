package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestEntity entity.
 *
 * When extending this class, extend TestEntityRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TestEntityRepository extends TestEntityRepositoryWithBagRelationships, JpaRepository<TestEntity, Long> {
    default Optional<TestEntity> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<TestEntity> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<TestEntity> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select testEntity from TestEntity testEntity left join fetch testEntity.themeFile",
        countQuery = "select count(testEntity) from TestEntity testEntity"
    )
    Page<TestEntity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select testEntity from TestEntity testEntity left join fetch testEntity.themeFile")
    List<TestEntity> findAllWithToOneRelationships();

    @Query("select testEntity from TestEntity testEntity left join fetch testEntity.themeFile where testEntity.id =:id")
    Optional<TestEntity> findOneWithToOneRelationships(@Param("id") Long id);
}
