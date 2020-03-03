package com.magnarox.batch.manager.batch;

import com.magnarox.batch.manager.entities.TutoPeople;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<TutoPeople, TutoPeople> {

    @Override
    public TutoPeople process(final TutoPeople person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final TutoPeople transformedPerson = new TutoPeople();

        transformedPerson.setPersonId(null);
        transformedPerson.setFirstName(firstName);
        transformedPerson.setLastName(lastName);

        System.out.println("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}
