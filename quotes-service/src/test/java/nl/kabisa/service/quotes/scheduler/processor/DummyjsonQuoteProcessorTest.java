package nl.kabisa.service.quotes.scheduler.processor;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.dummyjson.client.api.GetQuotesApi;
import nl.kabisa.service.quotes.dummyjson.client.model.QuoteDummyApi;
import nl.kabisa.service.quotes.mapper.QuoteDummyApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DummyjsonQuoteProcessorTest {

    @Mock
    private QuoteRepository quoteRepositoryMock;

    @Mock
    GetQuotesApi getQuotesApiMock;

    private static QuoteDummyApiMapper quoteDummyApiMapper = Mappers.getMapper(QuoteDummyApiMapper.class);

    @InjectMocks
    private DummyjsonQuoteProcessor dummyjsonQuoteProcessor;

    @BeforeEach
    public void beforeEach() {
        dummyjsonQuoteProcessor = new DummyjsonQuoteProcessor(quoteRepositoryMock, getQuotesApiMock, quoteDummyApiMapper);
    }

    @Test
    public void test_process_new_quote() {
        // Given
        QuoteDummyApi quoteDummyApi = new QuoteDummyApi();
        quoteDummyApi.setQuote("This is a test quote");

        when(quoteRepositoryMock.existsByText(anyString())).thenReturn(false);

        // When
        dummyjsonQuoteProcessor.processQuote(quoteDummyApi);

        // Then
        verify(quoteRepositoryMock, times(1)).save(any(QuoteEntity.class));
    }

    @Test
    public void test_process_existing_quote() {
        // Given
        QuoteDummyApi quoteDummyApi = new QuoteDummyApi();
        quoteDummyApi.setQuote("This is a test quote");

        when(quoteRepositoryMock.existsByText(anyString())).thenReturn(true);

        // When
        dummyjsonQuoteProcessor.processQuote(quoteDummyApi);

        // Then
        verify(quoteRepositoryMock, times(0)).save(any(QuoteEntity.class));
    }

}
