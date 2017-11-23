package camt.cbsd.lab05.service;

import camt.cbsd.lab05.dao.StudentDao;
import camt.cbsd.lab05.entity.RegisterEntity;
import camt.cbsd.lab05.entity.Student;
import camt.cbsd.lab05.entity.security.Authority;
import camt.cbsd.lab05.entity.security.AuthorityName;
import camt.cbsd.lab05.entity.security.User;
import camt.cbsd.lab05.security.repository.AuthorityRepository;
import camt.cbsd.lab05.security.repository.UserRepository;
import lombok.EqualsAndHashCode;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ConfigurationProperties(prefix = "server")
@Service
public class StudentServiceImpl implements StudentService {
    String imageBaseUrl;
    String baseUrl;
    String imageUrl;
    UserRepository userRepository;
    AuthorityRepository authorityRepository;
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @PostConstruct
    protected void setImageBaseUrl(){
        this.imageBaseUrl = this.baseUrl + this.imageUrl;
    }

    @Autowired
    StudentDao studentDao;
    public List<Student> getStudents(){

        return studentDao.getStudents();
    }

    @Override
    @Transactional
    public Student findById(long id) {
        Student student = studentDao.findById(id);
        Hibernate.initialize(student.getEnrolledCourse());
        return student;
    }

    @Override
    public Student addStudent(Student student) {
        return studentDao.addStudent(student);
    }

    @Override
    @Transactional
    public Student getStudentForTransfer(String username) {
        Student student = studentDao.findByUsername(username);
        Hibernate.initialize(student.getAuthorities());

        return student;
    }

    @Override
    @Transactional
    public List<Student> queryStudent(String query) {
        if (query == null || query.equals("")){
            return studentDao.getStudents();
        }
        return studentDao.getStudents(query);
    }
    @Transactional
    @Override
    public Student addStudent(RegisterEntity registerEntity){
        Authority authority;
        if(registerEntity.getRole().equals("Admin")){
            authority=authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
        }else {
            authority=authorityRepository.findByName(AuthorityName.ROLE_USER);
        }
        Student student=registerEntity.getStudent();
        User user=User.builder().username(registerEntity.getUsername())
                .password(registerEntity.getPassword())
                .firstname(student.getName())
                .lastname("default surname")
                .email("default @default")
                .lastPasswordResetDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .authorities(Arrays.asList(authority))
                .enabled(true)
                .build();
        student=studentDao.addStudent(student);
        user=userRepository.save(user);
        student.setUser(user);
        user.setStudent(student);

        Hibernate.initialize(student.getUser());
        Hibernate.initialize(student.getAuthorities());
        return student;
    }
}
