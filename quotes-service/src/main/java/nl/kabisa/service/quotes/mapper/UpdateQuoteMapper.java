package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.mapper.qualifier.QuoteQualifiers;
import nl.kabisa.service.quotes.rest.model.UpdateQuote;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = RatingMapper.class)
public interface UpdateQuoteMapper extends QuoteQualifiers {

    UpdateQuote toUpdateQuote(QuoteEntity quoteEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    void toQuoteEntity(UpdateQuote updateQuote, @MappingTarget QuoteEntity quoteEntity);



}
