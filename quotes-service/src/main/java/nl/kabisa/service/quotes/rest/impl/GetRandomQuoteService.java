package nl.kabisa.service.quotes.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.QuoteMapper;
import nl.kabisa.service.quotes.rest.api.GetQuotesApiDelegate;
import nl.kabisa.service.quotes.rest.api.GetRandomQuoteApiDelegate;
import nl.kabisa.service.quotes.rest.model.GetQuotesResult;
import nl.kabisa.service.quotes.rest.model.GetRandomQuoteResult;
import nl.kabisa.service.quotes.rest.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class GetRandomQuoteService implements GetRandomQuoteApiDelegate {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    @Override
    public ResponseEntity<GetRandomQuoteResult> getRandomQuote() {
        List<Quote> quoteList = quoteMapper.toQuoteList(quoteRepository.findByActive(true));

        GetRandomQuoteResult result = new GetRandomQuoteResult();
        if(!CollectionUtils.isEmpty(quoteList)) {
            int randomIndex = ThreadLocalRandom.current().nextInt(quoteList.size());
            result.setQuote(quoteList.get(randomIndex));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
