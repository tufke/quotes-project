package nl.kabisa.service.quotes.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.dummyjson.client.model.QuoteDummyApi;
import nl.kabisa.service.quotes.rest.model.Quote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteEntityTestUtil {
    private static Long id = 1L;

    private static Long getNextId() {
        return id++;
    }

    public static QuoteEntity default_with_no_ratings() {
        QuoteEntity entity =
              QuoteEntity.builder()
                .id(getNextId())
                .version(1L)
                .text("to quote, or not to quote")
                .author("Mark")
                .creationDate(LocalDate.of(2024, 11, 26))
                .ratings(new ArrayList<>()).build();
        return entity;
    }

    public static QuoteEntity createDeepCopy(QuoteEntity entity, ObjectMapper jacksonObjectMapper) throws JsonProcessingException {
        return jacksonObjectMapper.readValue(jacksonObjectMapper.writeValueAsString(entity), QuoteEntity.class);
    }

    public static List<QuoteEntity> createDeepCopy(List<QuoteEntity> entityList, ObjectMapper jacksonObjectMapper) throws JsonProcessingException {
        List<QuoteEntity> deepCopy = new ArrayList<>();
        for (QuoteEntity entity : entityList) {
            deepCopy.add(createDeepCopy(entity, jacksonObjectMapper));
        }

        return deepCopy;
    }

    public static boolean assertEqual(QuoteEntity quoteEntity, Quote quote) {
        assertThat(quote.getCreationDate()).isEqualTo(quoteEntity.getCreationDate());
        assertThat(quote.getActive()).isEqualTo(quoteEntity.isActive());
        assertThat(quote.getAuthor()).isEqualTo(quoteEntity.getAuthor());
        assertThat(quote.getText()).isEqualTo(quoteEntity.getText());
        assertThat(quote.getId()).isEqualTo(quoteEntity.getId());
        assertThat(quote.getVersion()).isEqualTo(quoteEntity.getVersion());

        return true;
    }

    public static boolean assertEqual(QuoteEntity quoteEntity, QuoteDummyApi quote) {
        assertThat(quote.getAuthor()).isEqualTo(quoteEntity.getAuthor());
        assertThat(quote.getQuote()).isEqualTo(quoteEntity.getText());

        return true;
    }
}
