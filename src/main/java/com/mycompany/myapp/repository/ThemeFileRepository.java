package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ThemeFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ThemeFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeFileRepository extends JpaRepository<ThemeFile, Long> {}
