package camt.cbsd.lab05.service;

import camt.cbsd.lab05.entity.RegisterEntity;
import camt.cbsd.lab05.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student findById(long id);
    Student addStudent(Student student);
    Student getStudentForTransfer(String username);
    List<Student> queryStudent(String query);
    Student addStudent(RegisterEntity registerEntity);
}
