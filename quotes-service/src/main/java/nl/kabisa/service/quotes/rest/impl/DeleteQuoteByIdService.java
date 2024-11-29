package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.rest.api.DeleteQuoteByIdApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class DeleteQuoteByIdService implements DeleteQuoteByIdApiDelegate {

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public ResponseEntity<Void> deleteQuoteById(Long quoteId) {
        //Find entity
        QuoteEntity quoteEntity = quoteRepository.findById(quoteId)
                .orElseThrow( ()-> new ValidationException("Quote not found by id: " + quoteId));

        quoteRepository.delete(quoteEntity);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
