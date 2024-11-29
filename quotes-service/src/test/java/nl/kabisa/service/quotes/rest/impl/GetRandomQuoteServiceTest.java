package nl.kabisa.service.quotes.rest.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.dummyjson.client.api.GetQuotesApi;
import nl.kabisa.service.quotes.mapper.QuoteDummyApiMapper;
import nl.kabisa.service.quotes.mapper.QuoteMapper;
import nl.kabisa.service.quotes.mapper.RatingMapper;
import nl.kabisa.service.quotes.rest.model.CreateQuote;
import nl.kabisa.service.quotes.rest.model.GetRandomQuoteResult;
import nl.kabisa.service.quotes.rest.model.Quote;
import nl.kabisa.service.quotes.scheduler.processor.DummyjsonQuoteProcessor;
import nl.kabisa.service.quotes.test.util.QuoteEntityTestUtil;
import nl.kabisa.service.quotes.test.util.QuoteRepositoryTestUtil;
import nl.kabisa.service.quotes.test.util.RatingEntityTestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class GetRandomQuoteServiceTest {

    @Mock
    QuoteRepository quoteRepositoryMock;

    private static QuoteMapper quoteMapper = Mappers.getMapper(QuoteMapper.class);
    private static RatingMapper ratingMapper = Mappers.getMapper(RatingMapper.class);

    @InjectMocks
    private GetRandomQuoteService getRandomQuoteService;

    @BeforeAll
    public static void beforeAll() {
        ReflectionTestUtils.setField(quoteMapper, "ratingMapper", ratingMapper);
    }

    @BeforeEach
    public void beforeEach() {
        getRandomQuoteService = new GetRandomQuoteService(quoteRepositoryMock, quoteMapper);
    }

    @Test
    public void test_getRadomQuote() throws JsonProcessingException {
        QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();

        //Configure mocks
        when(quoteRepositoryMock.findByActive(true)).thenReturn(new ArrayList<QuoteEntity>(Collections.singletonList(quoteEntity)));

        //Start the test
        ResponseEntity<GetRandomQuoteResult> getRandomQuoteResult = getRandomQuoteService.getRandomQuote();

        assertThat(getRandomQuoteResult).isNotNull();
        assertThat(getRandomQuoteResult.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(getRandomQuoteResult.getBody()).isNotNull();
        assertThat(getRandomQuoteResult.getBody().getQuote()).isNotNull();
        assertThat(getRandomQuoteResult.getBody().getQuote().getText()).isEqualTo(quoteEntity.getText());

        verifyNoMoreInteractions(quoteRepositoryMock);
    }

    @Test
    public void test_getRadomQuote_check_if_random() throws JsonProcessingException {
        List<QuoteEntity> quoteList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            QuoteEntity quoteEntity = QuoteRepositoryTestUtil.default_with_no_ratings();
            quoteEntity.setText(String.format("random quote %s", i));
            log.info(quoteEntity.getText());
            quoteList.add(quoteEntity);
        }

        //Configure mocks
        when(quoteRepositoryMock.findByActive(true)).thenReturn(quoteList);

        //Start the test
        GetRandomQuoteResult getRandomQuoteResult = getRandomQuoteService.getRandomQuote().getBody();
        Quote randomQuote = getRandomQuoteResult.getQuote();
        boolean differentQuoteFound = false;
        int totalDifferentQuotesFound = 0;
        for (int i = 1; i <= 100; i++) {
            Quote quote = getRandomQuoteService.getRandomQuote().getBody().getQuote();
            if(!quote.getText().equals(randomQuote.getText())) {
                differentQuoteFound = true;
                totalDifferentQuotesFound++;
                randomQuote = quote;
            }
        }

        assertThat(differentQuoteFound).isTrue();
        assertThat(totalDifferentQuotesFound).isGreaterThan(80);

        verifyNoMoreInteractions(quoteRepositoryMock);
    }
}