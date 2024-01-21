package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestUserRepository extends JpaRepository<TestUser, Long> {}
