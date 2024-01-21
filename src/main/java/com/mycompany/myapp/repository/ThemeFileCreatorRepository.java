package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ThemeFileCreator;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ThemeFileCreator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeFileCreatorRepository extends JpaRepository<ThemeFileCreator, Long> {}
