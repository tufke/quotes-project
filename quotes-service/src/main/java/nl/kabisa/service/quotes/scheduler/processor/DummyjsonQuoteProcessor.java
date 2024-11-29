package nl.kabisa.service.quotes.scheduler.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.dummyjson.client.api.GetQuotesApi;
import nl.kabisa.service.quotes.dummyjson.client.model.GetQuotesResultDummyApi;
import nl.kabisa.service.quotes.dummyjson.client.model.QuoteDummyApi;
import nl.kabisa.service.quotes.mapper.QuoteDummyApiMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DummyjsonQuoteProcessor {

    private final QuoteRepository quoteRepository;
    private final GetQuotesApi getQuotesApi;
    private final QuoteDummyApiMapper quoteDummyApiMapper;

    @Transactional
    public void processQuotes() {
        GetQuotesResultDummyApi getQuotesResultApi = getQuotesApi.getQuotes(0,0);
        log.info("retreived {} quotes from dummyjson.com", getQuotesResultApi.getQuotes().size());

        for(QuoteDummyApi quoteApi : getQuotesResultApi.getQuotes()) {
            processQuote(quoteApi);
        }
    }

    //@Async
    @Transactional
    public void processQuote(QuoteDummyApi quoteDummyApi) {

        if(StringUtils.isNotBlank(quoteDummyApi.getQuote())) {
            boolean quoteExists = quoteRepository.existsByText(quoteDummyApi.getQuote());
            if(!quoteExists){
                QuoteEntity quoteEntity = quoteDummyApiMapper.toQuoteEntity(quoteDummyApi);
                quoteRepository.save(quoteEntity);
                log.info("Added new quote to database: {}", quoteEntity);
            }
        }
    }

}
