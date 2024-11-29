package nl.kabisa.service.quotes.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.model.enums.RatingEnum;
import nl.kabisa.service.quotes.rest.model.Rating;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RatingEntityTestUtil {
    private static Long id = 1L;

    private static Long getNextId() {
        return id++;
    }

    public static RatingEntity default_rating() {
        RatingEntity entity =
             RatingEntity.builder()
                .id(getNextId())
                .version(1L)
                .rating(RatingEnum.AGREE)
                .userId("1")
                .build();
        return entity;
    }

     public static RatingEntity createDeepCopy(RatingEntity entity, ObjectMapper jacksonObjectMapper) throws JsonProcessingException {
        return jacksonObjectMapper.readValue(jacksonObjectMapper.writeValueAsString(entity), RatingEntity.class);
    }

    public static List<RatingEntity> createDeepCopy(List<RatingEntity> entityList, ObjectMapper jacksonObjectMapper) throws JsonProcessingException {
        List<RatingEntity> deepCopy = new ArrayList<>();
        for (RatingEntity entity : entityList) {
            deepCopy.add(createDeepCopy(entity, jacksonObjectMapper));
        }

        return deepCopy;
    }

    public static boolean assertEqual(RatingEntity entity, Rating model) {
        assertThat(model.getId()).isEqualTo(entity.getId());
        assertThat(model.getVersion()).isEqualTo(entity.getVersion());
        assertThat(model.getUserId()).isEqualTo(entity.getUserId());
        assertThat(model.getRating()).isEqualTo(entity.getRating().getAlfaCode());

        return true;
    }
}
