package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Autowired
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void TestThat_StudentService_GetAllStudents_CanGetAllStudents() {
        //When
        underTest.getAllStudents();

        //Then
        verify(studentRepository).findAll();
    }

    @Test
    void TestThat_StudentService_AddStudent_CanAddStudent() {
        //Given
        Student student = new Student(
                "Alice",
                "alice@gmail.com",
                Gender.FEMALE
        );

        //When
        underTest.addStudent(student);

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void TestThat_StudentService_AddStudent_WillThrowBadRequestException_WhenEmailIsTaken() {
        //Given
        Student student = new Student(
                "Alice",
                "alice@gmail.com",
                Gender.FEMALE
        );

        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(true);

        //When and Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void TestThat_StudentService_DeleteStudent_CanDeleteStudent() {
        //Given
        Student student = new Student(
                "Alice",
                "alice@gmail.com",
                Gender.FEMALE
        );

        given(studentRepository.existsById(any()))
                .willReturn(true);

        //When
        Long studentId = 1L;
        underTest.deleteStudent(studentId);

        //Then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void TestThat_StudentService_DeleteStudent_WillThrowStudentNotFoundException_WhenStudentNotFound() {
        //When and Then
        Long studentId = 1L;

        assertThatThrownBy(() -> underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(any());
    }
}