package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.mapper.UpdateQuoteMapper;
import nl.kabisa.service.quotes.rest.api.UpdateQuoteByIdApiDelegate;
import nl.kabisa.service.quotes.rest.model.UpdateQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UpdateQuoteByIdService implements UpdateQuoteByIdApiDelegate {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UpdateQuoteMapper quoteMapper;


    @Override
    public ResponseEntity<Void> updateQuoteById(Long quoteId, UpdateQuote updateQuote) {
        //Find entity
        QuoteEntity quoteEntity = quoteRepository.findById(quoteId)
                .orElseThrow( ()-> new ValidationException("Quote not found by id: " + quoteId));

        if (!quoteEntity.validateVersion(updateQuote.getVersion())) {
            throw new ValidationException("optimistic locking failed; Entity was updated by another transaction");
        }

        //Map input to entity
        quoteMapper.toQuoteEntity(updateQuote, quoteEntity);

        //Save entity
        quoteRepository.save(quoteEntity);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
