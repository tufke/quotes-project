package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.UpdateRatingMapper;
import nl.kabisa.service.quotes.rest.api.UpdateRatingByIdApiDelegate;
import nl.kabisa.service.quotes.rest.model.UpdateRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UpdateRatingByIdService implements UpdateRatingByIdApiDelegate {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UpdateRatingMapper updateRatingMapper;


    @Override
    public ResponseEntity<Void> updateRatingById(Long ratingId, UpdateRating updateRating) {
        //Find entity
        RatingEntity ratingEntity = ratingRepository.findById(ratingId)
                .orElseThrow( ()-> new ValidationException("Rating not found by id: " + ratingId));

        if (!ratingEntity.validateVersion(updateRating.getVersion())) {
            throw new ValidationException("optimistic locking failed; Entity was updated by another transaction");
        }

        String userId = ApplicationContextManager.getApplicationContext().getUserid();

        if (!userId.equals(ratingEntity.getUserId())) {
            throw new ValidationException(String.format("logged in user %s is not allowed to update a rating from user %s", userId, ratingEntity.getUserId()));
        }

        //Map input to entity
        updateRatingMapper.toRatingEntity(updateRating, ratingEntity);

        //Save entity
        ratingRepository.save(ratingEntity);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }


}
