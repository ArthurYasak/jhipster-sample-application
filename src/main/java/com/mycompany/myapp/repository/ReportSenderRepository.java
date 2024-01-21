package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReportSender;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportSender entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportSenderRepository extends JpaRepository<ReportSender, Long> {}
