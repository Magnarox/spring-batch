package com.magnarox.batch.batch;

import com.magnarox.batch.entities.TutoPeople;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements  ItemProcessor<TutoPeople, TutoPeople> {
    public static long i = 0;

    @Override
    public TutoPeople process(final TutoPeople person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final TutoPeople transformedPerson = new TutoPeople();

        transformedPerson.setPersonId(i++);
        transformedPerson.setFirstName(firstName);
        transformedPerson.setLastName(lastName);

        System.out.println("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}
