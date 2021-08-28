package com.example.demo;

import com.example.demo.dao.StudentRepository;
import com.example.demo.models.student.Address;
import com.example.demo.models.student.Gender;
import com.example.demo.models.student.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
    List<String> subjectList = new ArrayList<String>(Arrays.asList("IT"));

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address(
                    "India",
                    "Chennai",
                    "6000001"
            );
            String email = "keerrthu1@gmail.com";
            Student student = new Student(
                    "100",
                    "keerrthu",
                    "murali",
                    email,
                    Gender.FEMALE,
                    address,
                    subjectList,
                    BigDecimal.ONE,
                    LocalDateTime.now()

            );
            final Boolean flag = Boolean.FALSE;
//			usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);
//            usingMongoRepository(repository, email, student);
        };
    }

    private void usingMongoRepository(StudentRepository repository, String email, Student student) {
        if (repository.findStudentByEmail(email).isPresent()) {
            System.out.println(student + " already exists");
        } else {
            System.out.println("Inserting student " + student);
            repository.insert(student);
        }
    }

    private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalStateException("Found many student with email " + email);
        }

        if (students.isEmpty()) {
            System.out.println("Inserting student " + student);
            repository.insert(student);
        } else {
            System.out.println(student + " already exists");
        }
    }


}
