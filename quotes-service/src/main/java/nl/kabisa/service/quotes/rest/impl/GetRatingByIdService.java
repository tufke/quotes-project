package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.RatingMapper;
import nl.kabisa.service.quotes.rest.api.GetRatingByIdApiDelegate;
import nl.kabisa.service.quotes.rest.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class GetRatingByIdService implements GetRatingByIdApiDelegate {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public ResponseEntity<Rating> getRatingById(Long ratingId) {
        //Find entity
        RatingEntity ratingEntity = ratingRepository.findById(ratingId)
                .orElseThrow( ()-> new ValidationException("Rating not found by id: " + ratingId));

        Rating result = ratingMapper.toRating(ratingEntity);

        return new ResponseEntity<Rating>(result, HttpStatus.OK);
    }
}
