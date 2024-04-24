package xyz.oagueda.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.oagueda.domain.Pattern;

/**
 * Spring Data JPA repository for the Pattern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatternRepository extends JpaRepository<Pattern, Long>, JpaSpecificationExecutor<Pattern> {}
