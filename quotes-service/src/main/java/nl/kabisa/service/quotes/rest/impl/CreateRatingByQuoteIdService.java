package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.model.enums.RatingEnum;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.CreateRatingMapper;
import nl.kabisa.service.quotes.mapper.RatingMapper;
import nl.kabisa.service.quotes.rest.api.CreateRatingByQuoteIdApiDelegate;
import nl.kabisa.service.quotes.rest.model.CreateRating;
import nl.kabisa.service.quotes.rest.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CreateRatingByQuoteIdService implements CreateRatingByQuoteIdApiDelegate {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private CreateRatingMapper createRatingMapper;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public ResponseEntity<Rating> createRatingByQuoteId(Long quoteId, CreateRating createRating) {
        QuoteEntity quoteEntity = quoteRepository.findById(quoteId)
                .orElseThrow(()-> new ValidationException("Quote not found for id: " + quoteId));

        String userId = ApplicationContextManager.getApplicationContext().getUserid();

        for(RatingEntity ratingEntity : quoteEntity.getRatings()) {
            if(userId.equals(ratingEntity.getUserId())) {
                throw new ValidationException(String.format("Quote already rated by user %s", userId));
            }
        }

        RatingEntity ratingEntity = createRatingMapper.toRatingEntity(createRating);
        ratingEntity.setId(null);
        ratingEntity.setVersion(null);
        ratingEntity.setQuote(quoteEntity);
        ratingEntity.setUserId(userId);
        ratingEntity.setRating(RatingEnum.valueOfAlfaCode(createRating.getRating()));

        ratingEntity = ratingRepository.save(ratingEntity);
        Rating rating = ratingMapper.toRating(ratingEntity);

        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }


}
