package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.dummyjson.client.model.QuoteDummyApi;
import nl.kabisa.service.quotes.test.util.QuoteEntityTestUtil;
import nl.kabisa.service.quotes.test.util.RatingEntityTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteDummyApiMapperTest {

    QuoteDummyApiMapper quoteDummyApiMapper = Mappers.getMapper(QuoteDummyApiMapper.class);

    @Test
    public void test_QuoteDummyApiMapper_toQuoteDummyApi_and_inverse() {
        QuoteEntity quoteEntity = createQuoteEntity();

        QuoteDummyApi quoteDummyApi = quoteDummyApiMapper.toQuoteDummyApi(quoteEntity);
        Assertions.assertThat(QuoteEntityTestUtil.assertEqual(quoteEntity, quoteDummyApi)).isTrue();

        quoteEntity = quoteDummyApiMapper.toQuoteEntity(quoteDummyApi);
        assertThat(QuoteEntityTestUtil.assertEqual(quoteEntity, quoteDummyApi)).isTrue();
    }

    @Test
    public void test_QuoteDummyApiMapper_toQuoteDummyApiList_and_inverse() {
        List<QuoteEntity> quoteEntityList = new ArrayList<>();
        quoteEntityList.add(createQuoteEntity());
        quoteEntityList.add(createQuoteEntity());

        List<QuoteDummyApi> quoteDummyApiList = quoteDummyApiMapper.toQuoteDummyApiList(quoteEntityList);
        assertThat(quoteDummyApiList).isNotNull().hasSize(2);

        quoteEntityList = quoteDummyApiMapper.toQuoteEntityList(quoteDummyApiList);
        assertThat(quoteEntityList).isNotNull().hasSize(2);
    }

    private QuoteEntity createQuoteEntity() {
        QuoteEntity quoteEntity = QuoteEntityTestUtil.default_with_no_ratings();
        RatingEntity ratingEntity = RatingEntityTestUtil.default_rating();
        quoteEntity.addRating(ratingEntity);

        return quoteEntity;
    }


}
