package nl.kabisa.service.quotes.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AutoConfigureTestEntityManager
public class TestUtil {

    @Autowired
    private TestEntityManager testEntityManager;

    @Transactional
    public void flushContextAndDetachEntities() {
        testEntityManager.flush(); // forces synchronization to DB
        testEntityManager.clear(); // clears persistence context, all entities are now detached and can be fetched again
    }

    @Transactional
    public void flushContext() {
        testEntityManager.flush(); // forces synchronization to DB
    }
}
