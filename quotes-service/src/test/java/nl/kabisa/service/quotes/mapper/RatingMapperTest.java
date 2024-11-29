package nl.kabisa.service.quotes.mapper;


import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.rest.model.Rating;
import nl.kabisa.service.quotes.test.util.QuoteEntityTestUtil;
import nl.kabisa.service.quotes.test.util.RatingEntityTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class RatingMapperTest {

    RatingMapper ratingMapper = Mappers.getMapper(RatingMapper.class);

    @Test
    public void test_RatingMapper_toRating_and_inverse() {
        RatingEntity ratingEntity = createRatingEntity();

        Rating rating = ratingMapper.toRating(ratingEntity);
        Assertions.assertThat(RatingEntityTestUtil.assertEqual(ratingEntity, rating)).isTrue();

        ratingEntity = ratingMapper.toRatingtEntity(rating);
        assertThat(RatingEntityTestUtil.assertEqual(ratingEntity, rating)).isTrue();
    }

    private RatingEntity createRatingEntity() {
        QuoteEntity quoteEntity = QuoteEntityTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingEntityTestUtil.default_rating();
        ratingEntity.setQuote(quoteEntity);
        return ratingEntity;
    }


}