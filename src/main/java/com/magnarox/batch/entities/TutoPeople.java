package com.magnarox.batch.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "people", schema = "public", catalog = "postgres")
public class TutoPeople {
    private Long personId;
    private String firstName;
    private String lastName;

    @Id
    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutoPeople that = (TutoPeople) o;
        return Objects.equals(personId, that.personId) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, firstName, lastName);
    }
}
