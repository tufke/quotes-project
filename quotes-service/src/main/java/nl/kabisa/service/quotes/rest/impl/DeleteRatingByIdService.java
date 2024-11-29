package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.RatingMapper;
import nl.kabisa.service.quotes.rest.api.DeleteRatingByIdApiDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class DeleteRatingByIdService implements DeleteRatingByIdApiDelegate {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public ResponseEntity<Void> deleteRatingById(Long ratingId) {
        //Find entity
        RatingEntity ratingEntity = ratingRepository.findById(ratingId)
                .orElseThrow( ()-> new ValidationException("Rating not found by id: " + ratingId));

        String userId = ApplicationContextManager.getApplicationContext().getUserid();

        if (!userId.equals(ratingEntity.getUserId())) {
            throw new ValidationException(String.format("logged in user %s is not allowed to delete a rating from user %s", userId, ratingEntity.getUserId()));
        }

        ratingRepository.delete(ratingEntity);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
