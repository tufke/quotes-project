package nl.kabisa.service.quotes.rest.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.RatingMapper;
import nl.kabisa.service.quotes.test.util.QuoteEntityTestUtil;
import nl.kabisa.service.quotes.test.util.RatingEntityTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GetRatingsByQuoteIdServiceTest {

    @MockBean
    private RatingRepository ratingRepositoryMock;

    @MockBean
    private RatingMapper ratingMapperMock;

    @Autowired
    private GetRatingsByQuoteIdService getRatingsByQuoteIdService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    public void test_getRatingsByQuoteId() throws JsonProcessingException {
        //Prepare Input and expected result
        RatingEntity ratingEntity = RatingEntityTestUtil.default_rating();
        ratingEntity.setQuote(QuoteEntityTestUtil.default_with_no_ratings());
        List<RatingEntity> findByQuoteIdResult = Collections.singletonList(ratingEntity);

        /**
         * Make a copy of the findByQuoteIdResult List.
         * In the getRatingsByQuoteIdService the mocked findByQuoteId call to the repository returns findByQuoteIdResult List
         * During processing in getRatingsByQuoteIdService this list may not be changed
         * Finally compare the deep coppy expectedMapperInput to the input of the mapper at the end of processing to
         * assert if everything is unchanged.
         */
        List<RatingEntity> expectedMapperInput = RatingEntityTestUtil.createDeepCopy(findByQuoteIdResult, jacksonObjectMapper);

        //Configure mocks
        when(ratingRepositoryMock.findByQuoteId(isA(Long.class))).thenReturn(findByQuoteIdResult);
        when(ratingMapperMock.toRatingList(anyList())).thenReturn(null);

        //Start the test
        getRatingsByQuoteIdService.getRatingsByQuoteId(ratingEntity.getId());

        //Verify if result matches what we expected
        verify(ratingRepositoryMock).findByQuoteId(ratingEntity.getId());
        verify(ratingMapperMock).toRatingList(argThat( mapperInput -> {
            assertThat(mapperInput).isNotEmpty().hasSize(1);
            assertThat(expectedMapperInput).usingRecursiveComparison().isEqualTo(mapperInput);
            return true;
        }));
        verifyNoMoreInteractions(ratingRepositoryMock);
        verifyNoMoreInteractions(ratingMapperMock);
    }
}