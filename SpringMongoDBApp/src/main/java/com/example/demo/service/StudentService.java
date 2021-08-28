package com.example.demo.service;


import com.example.demo.dao.StudentRepository;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.student.Student;
import com.example.demo.util.UpdateUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class StudentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not exist with id: "+ id));
    }

    public String addStudent(Student student) {
        String result = "";
        try {
            if (!isStudentIdExists(student)) {
                studentRepository.insert(student);
                if (isStudentIdExists(student)) {
                    log.info("Inserted student " + student.getId() + " Successfully");
                    return ("Inserted student " + student.getId() + " Successfully");
                } else {
                    log.error("error while");
                    return ("error while");
                }
            } else {
                log.warn("student with Id :" + student.getId() + " already exists");
                return ("student with Id :" + student.getId() + " already exists");
            }

        } catch (Exception e) {
            result = e.toString();
            e.printStackTrace();
        }
        return result;
    }

    public Optional<Student> updateStudent(String id, Student student) {
        studentRepository.save(student);
        return studentRepository.findById(id);

    }

    public String deleteStudent(String id)
    {
        List<Student> studentsList =new ArrayList<>();
        studentRepository.deleteById(id);

        if(!isStudentIdExists(id))
        {
            return ("student with Id :" + id + " is removed successfully");
        }
        else
        {
            return ("student is not removed Id :" + id);
        }

    }

    public Optional<Student> updateStudentFields(String id, HashMap<String, Object> fields, String type) {
        updatedocumentField(id, fields, type);
        return studentRepository.findById(id);

    }

    private boolean isStudentIdExists(Student student) {
        if (studentRepository.findById(student.getId()).isPresent()) {
            System.out.println("The student with ID: " + student.getId() + " already exist");
            return true;
        } else {
            return false;
        }
    }
    private boolean isStudentIdExists(String id) {
        if (studentRepository.findById(id).isPresent()) {
            System.out.println("The student with ID: " +id + " already exist");
            return true;
        } else {
            return false;
        }
    }

    public void updatedocumentField(String id, HashMap<String, Object> fields, String type)
    {
        try {
            Class classType = UpdateUtility.getClassFromType(type); // Get the domain class from the type in the request
            Query query = new Query(Criteria.where("id").is(id)); // Update the document with the given ID
            Update update = new Update();

            // Iterate over the send fields and add them to the update object
            Iterator iterator = fields.entrySet().iterator();
            while(iterator.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) iterator.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                update.set(key, value);
            }


            mongoTemplate.updateFirst(query, update, classType); // Do the update
//            return mongoTemplate.findById(id, classType); // Return the updated document
        } catch (Exception e) {
            // Handle your exception
        }
//        return null;
    }


}
