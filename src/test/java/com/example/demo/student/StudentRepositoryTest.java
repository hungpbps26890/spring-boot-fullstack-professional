package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenStudentEmailExists() {
        //Given
        String email = "alice@gmail.com";
        Student student = new Student(
                "Alice",
                email,
                Gender.FEMALE
        );

        underTest.save(student);

        //When
        Boolean result = underTest.selectExistsEmail(email);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        //Given
        String email = "alice@gmail.com";

        //When
        Boolean result = underTest.selectExistsEmail(email);

        //Then
        assertThat(result).isFalse();
    }
}