package nl.kabisa.service.quotes.rest.impl;

import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.mapper.RatingMapper;
import nl.kabisa.service.quotes.rest.api.GetRatingsByQuoteIdApiDelegate;
import nl.kabisa.service.quotes.rest.model.GetRatingsByQuoteIdResult;
import nl.kabisa.service.quotes.rest.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class GetRatingsByQuoteIdService implements GetRatingsByQuoteIdApiDelegate {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public ResponseEntity<GetRatingsByQuoteIdResult> getRatingsByQuoteId(Long quoteId) {
        List<Rating> resultList = ratingMapper.toRatingList(ratingRepository.findByQuoteId(quoteId));

        GetRatingsByQuoteIdResult result = new GetRatingsByQuoteIdResult();
        result.setRatingList(resultList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
