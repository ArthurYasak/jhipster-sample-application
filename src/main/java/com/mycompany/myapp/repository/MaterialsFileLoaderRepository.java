package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialsFileLoader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialsFileLoader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialsFileLoaderRepository extends JpaRepository<MaterialsFileLoader, Long> {}
