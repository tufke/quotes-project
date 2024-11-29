package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.mapper.qualifier.QuoteQualifiers;
import nl.kabisa.service.quotes.rest.model.Quote;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = RatingMapper.class)
public interface QuoteMapper extends QuoteQualifiers {

    //mapping methods
    @Mapping(target = "ratings", ignore = false)
    Quote toQuote(QuoteEntity quoteEntity);

    @Mapping(target = "ratings", ignore = true)
    @InheritInverseConfiguration(name = "toQuote")
    QuoteEntity toQuoteEntity(Quote quote);

    @InheritConfiguration
    List<Quote> toQuoteList(List<QuoteEntity> quoteEntityList);

    @InheritConfiguration
    List<QuoteEntity> toQuoteEntityList(List<Quote> quoteList);



}
