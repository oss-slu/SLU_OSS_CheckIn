package com.checkin.controller;

import jakarta.inject.Singleton;
import jakarta.inject.Named;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
@Singleton
@Named("person_repo")
@JdbcRepository(dialect = Dialect.H2)
public interface PersonRepository extends CrudRepository<Person, Long> {
    void update(@Id Long id, String firstName, Boolean checkedIn);
    Person save(String firstName, Boolean checkedIn);

    List<String> findFirstNameByCheckedInTrue();

    List<String> findFirstNameByCheckedInFalse();
    @Query("SELECT p.first_name FROM person p")
    List<String> findAllFirstNames();
}
