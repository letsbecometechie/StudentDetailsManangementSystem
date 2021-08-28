package com.example.demo.controller;

import com.example.demo.service.StudentService;
import com.example.demo.models.student.Student;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/")
public class StudentController {

    @Autowired
    private final StudentService studentService;

    @RequestMapping(value = "students", method = RequestMethod.GET)
    public List<Student> fechtAllstudents() {
        return studentService.getAllStudents();
    }

    @RequestMapping(value = "student/{id}", method = RequestMethod.GET)
    public ResponseEntity<Student> fechtstudentById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @RequestMapping(value = "student", method = RequestMethod.POST)
    public String addStudentdetail(@RequestBody Student student) {
        if(student.getId() == null || student.getId().isEmpty())
        {
            student.setId(UUID.randomUUID().toString());
        }
        String result = studentService.addStudent(student);
        return result;
//         "Successfully inserted the data";
    }

//    @RequestMapping(value="/api/v1/student/{id}", method = RequestMethod.PUT)
//    public Optional<Student> updateStudentdetail(@PathVariable String id, @RequestBody Student student)
//    {
//        return studentService.updateStudent(id, student);
//    }

    @RequestMapping(value="student/{id}", method = RequestMethod.DELETE)
    public String deleteStudentdetail(@PathVariable String id)
    {
        return studentService.deleteStudent(id);
    }

    @RequestMapping(value="student/{id}", method = RequestMethod.PUT)
    public Optional<Student> updateStudentFields(@PathVariable String id, @RequestBody HashMap<String, Object> fields)
    {
        return studentService.updateStudentFields(id, fields,"student");
    }
}
