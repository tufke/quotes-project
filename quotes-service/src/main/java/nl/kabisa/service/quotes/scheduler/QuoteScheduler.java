package nl.kabisa.service.quotes.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kabisa.service.quotes.scheduler.processor.DummyjsonQuoteProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class QuoteScheduler {

    private final DummyjsonQuoteProcessor dummyjsonQuoteProcessor;

    /**
     * Scheduled method which is called n times (based on the cron input in config)
     * default is set to once a day at 4am
     * <p>
     * Starts the processor
     */
    @Scheduled(cron = "${scheduler.dummyjson.quote-processor.cron:0 0 4 * * ?}")
    @EventListener(ApplicationReadyEvent.class)
    public void processQuotes() {
        log.info("Started processing quotes");
        this.dummyjsonQuoteProcessor.processQuotes();
        log.info("Completed processing quotes");
    }
}
