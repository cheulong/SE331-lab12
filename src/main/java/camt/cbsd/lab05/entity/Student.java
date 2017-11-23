package camt.cbsd.lab05.entity;

import camt.cbsd.lab05.config.json.View;
import camt.cbsd.lab05.entity.security.Authority;
import camt.cbsd.lab05.entity.security.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@JsonIgnoreType
@EqualsAndHashCode
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @JsonView(View.Login.class)
    String studentId;
    @JsonView(View.Login.class)
    String name;
    @JsonView(View.Login.class)
    String surname;
    double gpa;
    @JsonView(View.Login.class)
    String image;
    boolean feature;
    int penAmount;
    String description;

    @OneToOne(mappedBy = "student")
    @JsonManagedReference
    User user;

    @ManyToMany
    List<Course> enrolledCourse;

    public List<Course> addCourse(Course course) {
        enrolledCourse = Optional.ofNullable(enrolledCourse).orElse(new ArrayList<>());
        enrolledCourse.add(course);
        return enrolledCourse;

    }
    @JsonView(View.Login.class)
    public List<Authority> getAuthorities(){
        return user.getAuthorities();
    }
}
