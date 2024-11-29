package nl.kabisa.service.quotes.test.util;

import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.model.enums.RatingEnum;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RatingRepositoryTestUtil {

    private static int userlid = 1;

    public static RatingEntity default_rating() {
        return RatingEntity.builder()
            .rating(RatingEnum.AGREE)
            .userId(Integer.toString(userlid++))
            .build();
    }

     public static void assertValidSaved(RatingEntity ratingEntity) {
        assertThat(ratingEntity.getId()).isNotNull();
        assertThat(ratingEntity.getVersion()).isNotNull();
        assertThat(ratingEntity.getQuote()).isNotNull();
        assertThat(ratingEntity.getQuote().getId()).isNotNull();
    }

    public static void assertValidFindAll(List<RatingEntity> entityList, int numberOfEntitiesExpected) {
        assertThat(entityList).isNotNull();
        assertThat(entityList).hasSize(numberOfEntitiesExpected);
        entityList.forEach(entity -> assertValidFindById(entity));
    }

    public static void assertValidFindById(RatingEntity entity) {
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getVersion()).isNotNull();
        assertThat(entity.getId()).isGreaterThan(0L);
        assertThat(entity.getVersion()).isGreaterThanOrEqualTo(0L);
    }
}
