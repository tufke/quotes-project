package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.mapper.QuoteMapper;
import nl.kabisa.service.quotes.rest.api.GetQuoteByIdApiDelegate;
import nl.kabisa.service.quotes.rest.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class GetQuoteByIdService implements GetQuoteByIdApiDelegate {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteMapper quoteMapper;

    @Override
    public ResponseEntity<Quote> getQuoteById(Long quoteId) {
        //Find entity
        QuoteEntity quoteEntity = quoteRepository.findById(quoteId)
                .orElseThrow( ()-> new ValidationException("Quote not found by id: " + quoteId));

        Quote result = quoteMapper.toQuote(quoteEntity);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
