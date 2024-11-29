package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.mapper.qualifier.RatingQualifiers;
import nl.kabisa.service.quotes.rest.model.Rating;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = {QuoteMapper.class})
public interface RatingMapper extends RatingQualifiers {

    @Mapping(source = "rating", target = "rating", qualifiedByName = "ratingEnumToString")
    Rating toRating(RatingEntity ratingEntity);

    @Mapping(source = "rating", target = "rating", qualifiedByName = "stringToRatingEnum")
    @Mapping(target = "quote", ignore = true)
    @InheritInverseConfiguration(name = "toRating")
    RatingEntity toRatingtEntity(Rating rating);

    @InheritConfiguration
    List<Rating> toRatingList(List<RatingEntity> ratingEntityList);

    @InheritConfiguration
    List<RatingEntity> toRatingEntityList(List<Rating> ratingList);

}
