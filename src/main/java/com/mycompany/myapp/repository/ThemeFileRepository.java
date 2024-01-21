package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ThemeFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ThemeFile entity.
 */
@Repository
public interface ThemeFileRepository extends JpaRepository<ThemeFile, Long> {
    default Optional<ThemeFile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ThemeFile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ThemeFile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select themeFile from ThemeFile themeFile left join fetch themeFile.materialsFile",
        countQuery = "select count(themeFile) from ThemeFile themeFile"
    )
    Page<ThemeFile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select themeFile from ThemeFile themeFile left join fetch themeFile.materialsFile")
    List<ThemeFile> findAllWithToOneRelationships();

    @Query("select themeFile from ThemeFile themeFile left join fetch themeFile.materialsFile where themeFile.id =:id")
    Optional<ThemeFile> findOneWithToOneRelationships(@Param("id") Long id);
}
