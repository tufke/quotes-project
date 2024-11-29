package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.mapper.qualifier.QuoteQualifiers;
import nl.kabisa.service.quotes.rest.model.CreateQuote;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = RatingMapper.class)
public interface CreateQuoteMapper extends QuoteQualifiers {

    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    QuoteEntity toQuoteEntity(CreateQuote createQuote);

}
