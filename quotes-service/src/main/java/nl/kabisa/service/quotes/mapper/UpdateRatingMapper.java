package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.mapper.qualifier.RatingQualifiers;
import nl.kabisa.service.quotes.rest.model.UpdateRating;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = {QuoteMapper.class})
public interface UpdateRatingMapper extends RatingQualifiers {

    @Mapping(source = "rating", target = "rating", qualifiedByName = "ratingEnumToString")
    UpdateRating toUpdateRating(RatingEntity ratingEntity);

    @Mapping(source = "rating", target = "rating", qualifiedByName = "stringToRatingEnum")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quote", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void toRatingEntity(UpdateRating updateRating, @MappingTarget RatingEntity ratingEntity);


}
