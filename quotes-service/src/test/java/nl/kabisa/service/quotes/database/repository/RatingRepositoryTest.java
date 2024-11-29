package nl.kabisa.service.quotes.database.repository;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.test.util.QuoteRepositoryTestUtil;
import nl.kabisa.service.quotes.test.util.RatingRepositoryTestUtil;
import nl.kabisa.service.quotes.test.util.TestUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.flyway.enabled=true"
})
public class RatingRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void test_save() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingRepositoryTestUtil.default_rating();
        ratingEntity.setQuote(quoteEntity);
        ratingRepository.save(ratingEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidSaved(quoteEntity, 1);
        RatingRepositoryTestUtil.assertValidSaved(ratingEntity);
    }

    @Test
    public void test_save_without_quote() {
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
            RatingEntity ratingEntity = RatingRepositoryTestUtil.default_rating();
            ratingEntity.setQuote(null);
            ratingRepository.saveAndFlush(ratingEntity);
        });

        Throwable cause = ex.getCause();
        assertThat(cause).isExactlyInstanceOf(ConstraintViolationException.class);
        cause = cause.getCause();
        assertThat(cause).isExactlyInstanceOf(SQLIntegrityConstraintViolationException.class);
        assertThat(cause.getMessage()).contains("NOT NULL check constraint");
        assertThat(cause.getMessage()).contains("table: RATING column: QUOTE_ID");
    }

    @Test
    public void test_findAll() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingRepositoryTestUtil.default_rating();
        ratingEntity.setQuote(quoteEntity);
        ratingRepository.save(ratingEntity);
        testUtil.flushContextAndDetachEntities();

        RatingRepositoryTestUtil.assertValidSaved(ratingEntity);

        List<RatingEntity> savedratingen = ratingRepository.findAll();
        RatingRepositoryTestUtil.assertValidFindAll(savedratingen, 1);

        RatingEntity savedRatingEntity = savedratingen.get(0);
    }

    @Test
    public void test_delete() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingRepositoryTestUtil.default_rating();
        ratingEntity.setQuote(quoteEntity);
        ratingRepository.save(ratingEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidSaved(quoteEntity, 1);
        RatingRepositoryTestUtil.assertValidSaved(ratingEntity);

        ratingRepository.delete(ratingEntity);
        testUtil.flushContextAndDetachEntities();
        QuoteRepositoryTestUtil.assertValidFindAll(quoteRepository.findAll(), 1);
        RatingRepositoryTestUtil.assertValidFindAll(ratingRepository.findAll(), 0);
    }


}