package nl.kabisa.service.quotes.database.repository;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository which can be used to access/manipulate {@link QuoteEntity} records in the database.
 *
 * https://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#repository-query-keywords
 */
@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {

    List<QuoteEntity> findByActive(boolean active);

    boolean existsByText(String text);


}
