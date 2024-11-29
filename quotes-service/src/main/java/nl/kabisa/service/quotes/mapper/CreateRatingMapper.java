package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.mapper.qualifier.RatingQualifiers;
import nl.kabisa.service.quotes.rest.model.CreateRating;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = {QuoteMapper.class})
public interface CreateRatingMapper extends RatingQualifiers {

    @Mapping(source = "rating", target = "rating", qualifiedByName = "ratingEnumToString")
    CreateRating toCreateRating(RatingEntity ratingEntity);

    @Mapping(source = "rating", target = "rating", qualifiedByName = "stringToRatingEnum")
    @Mapping(target = "quote", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "userId", ignore = true)
    RatingEntity toRatingEntity(CreateRating rating);





}
