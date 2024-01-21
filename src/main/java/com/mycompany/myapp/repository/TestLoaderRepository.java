package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestLoader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestLoader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestLoaderRepository extends JpaRepository<TestLoader, Long> {}
