package nl.kabisa.service.quotes.rest.impl;

import nl.kabisa.service.quotes.database.model.QuoteEntity;
import nl.kabisa.service.quotes.database.repository.QuoteRepository;
import nl.kabisa.service.quotes.rest.model.CreateQuote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CreateQuoteServiceTest {

    @MockBean
    private QuoteRepository quoteRepositoryMock;

    @Autowired
    private CreateQuoteService createQuoteService;

    @Test
    public void test_createQuote(){
        //Prepare Input and expected result
        CreateQuote input = new CreateQuote();
        input.setAuthor("Mark");
        input.setText("quote text");

        //Configure mocks
        when(quoteRepositoryMock.save(isA(QuoteEntity.class))).thenReturn(null);

        //Start the test
        createQuoteService.createQuote(input);

        //Verify if result matches what we expected
        verify(quoteRepositoryMock).save(argThat( saveInput -> {
            assertThat(saveInput).isNotNull();
            assertThat(saveInput.getId()).isNull();
            assertThat(saveInput.getVersion()).isNull();
            assertThat(saveInput.getCreationDate()).isEqualTo(LocalDate.now());
            assertThat(saveInput.getAuthor()).isEqualTo(input.getAuthor());
            assertThat(saveInput.getText()).isEqualTo(input.getText());
            assertThat(saveInput.isActive()).isEqualTo(true);
            return true;
        }));
        verifyNoMoreInteractions(quoteRepositoryMock);

    }

}