package nl.kabisa.service.quotes.rest.impl;

import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.QuoteMapper;
import nl.kabisa.service.quotes.rest.api.GetQuotesApiDelegate;
import nl.kabisa.service.quotes.rest.model.GetQuotesResult;
import nl.kabisa.service.quotes.rest.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@Slf4j
public class GetQuotesService implements GetQuotesApiDelegate {

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    QuoteMapper quoteMapper;

    @Override
    public ResponseEntity<GetQuotesResult> getQuotes() {
        List<Quote> quoteList = quoteMapper.toQuoteList(quoteRepository.findAll());

        GetQuotesResult result = new GetQuotesResult();
        result.setQuoteList(quoteList);

        return new ResponseEntity<GetQuotesResult>(result, HttpStatus.OK);
    }
}
