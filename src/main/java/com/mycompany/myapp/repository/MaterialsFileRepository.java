package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialsFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialsFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialsFileRepository extends JpaRepository<MaterialsFile, Long> {}
