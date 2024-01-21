package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestCreator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCreator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCreatorRepository extends JpaRepository<TestCreator, Long> {}
