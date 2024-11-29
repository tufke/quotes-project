package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.rest.model.Quote;
import nl.kabisa.service.quotes.scheduler.processor.DummyjsonQuoteProcessor;
import nl.kabisa.service.quotes.test.util.QuoteEntityTestUtil;
import nl.kabisa.service.quotes.test.util.RatingEntityTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteMapperTest {

    private static QuoteMapper quoteMapper = Mappers.getMapper(QuoteMapper.class);
    private static RatingMapper ratingMapper = Mappers.getMapper(RatingMapper.class);

    @BeforeAll
    public static void setUp() {
        ReflectionTestUtils.setField(quoteMapper, "ratingMapper", ratingMapper);
    }

    @Test
    public void test_QuoteMapper_toQuote_and_inverse() {
        QuoteEntity quoteEntity = createQuoteEntity();

        Quote quote = quoteMapper.toQuote(quoteEntity);
        Assertions.assertThat(QuoteEntityTestUtil.assertEqual(quoteEntity, quote)).isTrue();

        quoteEntity = quoteMapper.toQuoteEntity(quote);
        assertThat(QuoteEntityTestUtil.assertEqual(quoteEntity, quote)).isTrue();
    }

    @Test
    public void test_QuoteMapper_toQuoteList_and_inverse() {
        List<QuoteEntity> quoteEntityList = new ArrayList<>();
        quoteEntityList.add(createQuoteEntity());
        quoteEntityList.add(createQuoteEntity());

        List<Quote> quoteList = quoteMapper.toQuoteList(quoteEntityList);
        assertThat(quoteList).isNotNull().hasSize(2);

        quoteEntityList = quoteMapper.toQuoteEntityList(quoteList);
        assertThat(quoteEntityList).isNotNull().hasSize(2);
    }

    private QuoteEntity createQuoteEntity() {
        QuoteEntity quoteEntity = QuoteEntityTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingEntityTestUtil.default_rating();
        quoteEntity.addRating(ratingEntity);

        return quoteEntity;
    }


}
