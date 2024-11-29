package nl.kabisa.service.quotes.database.repository;


import nl.kabisa.service.quotes.database.model.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository which can be used to access/manipulate {@link RatingEntity} records in the database.
 *
 * https://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#repository-query-keywords
 */
@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {

    List<RatingEntity> findByQuoteId(Long quoteId);



}
