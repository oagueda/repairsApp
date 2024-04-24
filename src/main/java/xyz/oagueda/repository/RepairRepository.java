package xyz.oagueda.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.oagueda.domain.Repair;

/**
 * Spring Data JPA repository for the Repair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepairRepository extends JpaRepository<Repair, Long>, JpaSpecificationExecutor<Repair> {}
