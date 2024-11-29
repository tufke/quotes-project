package nl.kabisa.service.quotes.mapper;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.dummyjson.client.model.QuoteDummyApi;
import nl.kabisa.service.quotes.mapper.qualifier.QuoteQualifiers;
import nl.kabisa.service.quotes.rest.model.Quote;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        builder = @Builder(disableBuilder = true),
        uses = RatingMapper.class)
public interface QuoteDummyApiMapper extends QuoteQualifiers {

    //mapping methods
    @Mapping(source = "text", target = "quote")
    @Mapping(target = "id", ignore = true)
    QuoteDummyApi toQuoteDummyApi(QuoteEntity quoteEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @InheritInverseConfiguration(name = "toQuoteDummyApi")
    QuoteEntity toQuoteEntity(QuoteDummyApi quote);

    @InheritConfiguration
    List<QuoteDummyApi> toQuoteDummyApiList(List<QuoteEntity> quoteEntityList);

    @InheritConfiguration
    List<QuoteEntity> toQuoteEntityList(List<QuoteDummyApi> quoteDummyApiList);



}
