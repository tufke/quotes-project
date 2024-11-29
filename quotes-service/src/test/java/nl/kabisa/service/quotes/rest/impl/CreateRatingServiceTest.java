package nl.kabisa.service.quotes.rest.impl;

import jakarta.validation.ValidationException;
import nl.kabisa.service.quotes.context.ApplicationContext;
import nl.kabisa.service.quotes.context.ApplicationContextManager;
import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.model.RatingEntity;
import nl.kabisa.service.quotes.database.model.enums.RatingEnum;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.database.repository.RatingRepository;
import nl.kabisa.service.quotes.rest.model.CreateRating;
import nl.kabisa.service.quotes.test.util.QuoteEntityTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class CreateRatingServiceTest {

    @MockBean
    private QuoteRepository quoteRepositoryMock;

    @MockBean
    private RatingRepository ratingRepositoryMock;

    @Autowired
    private CreateRatingByQuoteIdService createRatingService;

    @BeforeEach
    public void setUp() {
        ApplicationContextManager.setApplicationContext(new ApplicationContext("mark", "securityToken"));
    }

    @Test
    public void test_createRating() {
        //Prepare Input and expected result
        CreateRating input = new CreateRating();
        input.setRating(RatingEnum.AGREE.getAlfaCode());

        QuoteEntity quoteEntity = QuoteEntityTestUtil.default_with_no_ratings();

        //Configure mocks
        when(quoteRepositoryMock.findById(isA(Long.class))).thenReturn(Optional.of(quoteEntity));
        when(ratingRepositoryMock.save(isA(RatingEntity.class))).thenReturn(null);

        //Start the test
        createRatingService.createRatingByQuoteId(quoteEntity.getId(), input);

        //Verify if result matches what we expected
        verify(quoteRepositoryMock).findById(quoteEntity.getId());
        verify(ratingRepositoryMock).save(argThat( saveInput -> {
            assertThat(saveInput).isNotNull();
            assertThat(saveInput.getId()).isNull();
            assertThat(saveInput.getVersion()).isNull();
            assertThat(saveInput.getQuote()).isNotNull();
            assertThat(saveInput.getRating()).isNotNull().isEqualTo(RatingEnum.valueOfAlfaCode(input.getRating()));
            assertThat(saveInput.getQuote().getId()).isNotNull().isEqualTo(quoteEntity.getId());
            return true;
        }));
        verifyNoMoreInteractions(quoteRepositoryMock);
        verifyNoMoreInteractions(ratingRepositoryMock);
    }

    @Test
    public void test_createRating_Exception() {
        //Prepare Input and expected result
        CreateRating input = new CreateRating();
        QuoteEntity quoteEntity = QuoteEntityTestUtil.default_with_no_ratings();

        //Configure mocks
        when(quoteRepositoryMock.findById(isA(Long.class))).thenReturn(Optional.ofNullable(null));

        //Start the test
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createRatingService.createRatingByQuoteId(quoteEntity.getId(), input);
        });

        assertThat(ex).isInstanceOf(ValidationException.class);
        assertThat(ex.getMessage()).isEqualTo("Quote not found for id: " + quoteEntity.getId());

        //Verify if result matches what we expected
        verify(quoteRepositoryMock).findById(quoteEntity.getId());
        verifyNoMoreInteractions(quoteRepositoryMock);
        verifyNoMoreInteractions(ratingRepositoryMock);
    }

}