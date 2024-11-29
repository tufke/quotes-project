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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.flyway.enabled=true",
})
public class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void test_save_quote() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidSaved(quoteEntity, 0);
    }

    @Test
    public void test_save_quote_with_rating() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingRepositoryTestUtil.default_rating();
        quoteEntity.addRating(ratingEntity);
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidSaved(quoteEntity, 1);
        RatingRepositoryTestUtil.assertValidSaved(ratingEntity);
    }

    @Test
    public void test_optimistic_locking_version_conflict() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidSaved(quoteEntity, 0);

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            quoteEntity.setVersion(123L);
            quoteRepository.save(quoteEntity);
        });
    }


    @Test
    public void test_validation_creationdate() {
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
            QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
            quoteEntity.setCreationDate(null);
            quoteRepository.saveAndFlush(quoteEntity);
        });

        Throwable cause = ex.getCause();
        assertThat(cause).isExactlyInstanceOf(ConstraintViolationException.class);
        cause = cause.getCause();
        assertThat(cause).isExactlyInstanceOf(SQLIntegrityConstraintViolationException.class);
        assertThat(cause.getMessage()).contains("NOT NULL check constraint");
        assertThat(cause.getMessage()).contains("table: QUOTE column: CREATION_DATE");
     }


    @Test
    public void test_delete_rating() {
        // 1 quote
        // 3 ratings
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        quoteEntity.addRating(RatingRepositoryTestUtil.default_rating());
        quoteEntity.addRating(RatingRepositoryTestUtil.default_rating());
        quoteEntity.addRating(RatingRepositoryTestUtil.default_rating());
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidFindAll(quoteRepository.findAll(), 1);
        RatingRepositoryTestUtil.assertValidFindAll(ratingRepository.findAll(), 3);

        //Remove 1 rating and save quote
        quoteEntity.removeRating(quoteEntity.getRatings().get(0));
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidFindAll(quoteRepository.findAll(), 1);
        RatingRepositoryTestUtil.assertValidFindAll(ratingRepository.findAll(), 2);
     }

    @Test
    public void test_save_multiple_new_ratings_via_quote() {
        // 1 quote
        // 3 ratings
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        quoteEntity.addRating(RatingRepositoryTestUtil.default_rating());
        quoteEntity.addRating(RatingRepositoryTestUtil.default_rating());
        quoteEntity.addRating(RatingRepositoryTestUtil.default_rating());
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        List<QuoteEntity> savedQuotes = quoteRepository.findAll();
        QuoteRepositoryTestUtil.assertValidFindAll(savedQuotes, 1);
        RatingRepositoryTestUtil.assertValidFindAll(savedQuotes.get(0).getRatings(), 3);
    }


    @Test
    public void test_findAll_with_rating() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        LocalDate creationDate = LocalDate.of(2024, Month.NOVEMBER, 26);
        quoteEntity.setCreationDate(creationDate);
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        List<QuoteEntity> savedQuotes = quoteRepository.findAll();
        QuoteRepositoryTestUtil.assertValidFindAll(savedQuotes, 1);

        QuoteEntity savedQuoteEntity = savedQuotes.get(0);
        assertThat(savedQuoteEntity.getCreationDate()).isEqualTo(creationDate);
    }

    @Test
    public void test_existsByText_true() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        boolean exists = quoteRepository.existsByText(quoteEntity.getText());
        assertThat(exists).isTrue();

    }

    @Test
    public void test_existsByText_false() {
        boolean exists = quoteRepository.existsByText("not existing quote");
        assertThat(exists).isFalse();
    }

    @Test
    public void test_delete_with_rating() {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingRepositoryTestUtil.default_rating();
        quoteEntity.addRating(ratingEntity);
        quoteRepository.save(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidSaved(quoteEntity, 1);
        RatingRepositoryTestUtil.assertValidSaved(ratingEntity);

        quoteRepository.delete(quoteEntity);
        testUtil.flushContextAndDetachEntities();

        QuoteRepositoryTestUtil.assertValidFindAll(quoteRepository.findAll(), 0);
        RatingRepositoryTestUtil.assertValidFindAll(ratingRepository.findAll(), 0);
    }


}