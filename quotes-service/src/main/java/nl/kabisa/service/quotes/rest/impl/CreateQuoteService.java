package nl.kabisa.service.quotes.rest.impl;

import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.mapper.CreateQuoteMapper;
import nl.kabisa.service.quotes.mapper.QuoteMapper;
import nl.kabisa.service.quotes.rest.api.CreateQuoteApiDelegate;
import nl.kabisa.service.quotes.rest.model.CreateQuote;
import nl.kabisa.service.quotes.rest.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@Slf4j
public class CreateQuoteService implements CreateQuoteApiDelegate {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CreateQuoteMapper createQuoteMapper;

    @Autowired
    private QuoteMapper quoteMapper;

    @Override
    public ResponseEntity<Quote> createQuote(CreateQuote createQuote) {
        QuoteEntity quoteEntity = createQuoteMapper.toQuoteEntity(createQuote);
        quoteEntity.setId(null);
        quoteEntity.setVersion(null);
        quoteEntity.setCreationDate(LocalDate.now());

        quoteEntity = quoteRepository.save(quoteEntity);
        Quote quote = quoteMapper.toQuote(quoteEntity);

        return new ResponseEntity<Quote>(quote, HttpStatus.CREATED);
    }
}
