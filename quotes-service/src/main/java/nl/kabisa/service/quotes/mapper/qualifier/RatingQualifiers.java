package nl.kabisa.service.quotes.mapper.qualifier;

import nl.kabisa.service.quotes.database.model.enums.RatingEnum;
import org.mapstruct.Named;

public interface RatingQualifiers {

    @Named("ratingEnumToString")
    static String ratingEnumToString(RatingEnum ratingEnum) {
        return ratingEnum.getAlfaCode();
    }

    @Named("stringToRatingEnum")
    static RatingEnum stringToRatingEnum(String ratingAlfaCode) {
        return RatingEnum.valueOfAlfaCode(ratingAlfaCode);
    }

}
