package nl.kabisa.service.quotes.test.util;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteRepositoryTestUtil {

    public static QuoteEntity default_with_no_ratings() {
        return QuoteEntity.builder()
            .creationDate(LocalDate.of(2024, 11, 26))
            .author("Mark")
            .text("To quote, or not to quote")
            .active(true)
            .ratings(new ArrayList<>()).build();
    }

    public static void assertValidSaved(QuoteEntity quoteEntity, int numberOfRatingsExpected) {
        assertThat(quoteEntity.getId()).isNotNull();
        assertThat(quoteEntity.getRatings()).isNotNull();
        assertThat(quoteEntity.getRatings()).hasSize(numberOfRatingsExpected);
        if (numberOfRatingsExpected > 0) {
            for (RatingEntity ratingEntity : quoteEntity.getRatings()) {
                assertThat(quoteEntity.getId()).isEqualTo(ratingEntity.getQuote().getId());
            }
        }
    }

    public static void assertValidFindAll(List<QuoteEntity> entityList, int numberOfEntitiesExpected) {
        assertThat(entityList).isNotNull();
        assertThat(entityList).hasSize(numberOfEntitiesExpected);
        entityList.forEach(entity -> assertValidFindById(entity));

        if(entityList.size() > 0) {
            Set<Long> ratingIds = new HashSet<>();
            for(QuoteEntity quote : entityList) {
                quote.getRatings().forEach(rating -> ratingIds.add(rating.getId()));
                assertThat(ratingIds).hasSize(quote.getRatings().size());
            }
        }
    }

    public static void assertValidFindById(QuoteEntity entity) {
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getVersion()).isNotNull();
        assertThat(entity.getId()).isGreaterThan(0L);
        assertThat(entity.getVersion()).isGreaterThanOrEqualTo(0L);
    }


}
