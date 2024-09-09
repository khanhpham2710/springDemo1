package com.example.demo.controllers;


import com.example.demo.dtos.StudentDTO;
import com.example.demo.models.Student;
import com.example.demo.responses.StudentListResponse;
import com.example.demo.responses.StudentResponse;
import com.example.demo.services.StudentService;
import jakarta.validation.Valid;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/student")

public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<Student> students = studentService.showAll();
        List<StudentDTO> responses = students.stream()
                .map(StudentDTO::mapToStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<StudentResponse> getByID(@PathVariable(name = "id") long id) {
        Student student = studentService.findById(id);
        if (student == null){
            throw new OpenApiResourceNotFoundException("Student not found with ID " + id);
        }
        return ResponseEntity.ok().body(StudentResponse.mapToResponse(student));
    }

    @PostMapping("add")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Student student = studentService.add(studentDTO);
        return ResponseEntity.ok().body(StudentResponse.builder()
                .name(student.getName())
                .city(student.getCity())
                .dob(student.getDob())
                .rank(student.getRank())
                .build());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable(name = "id") long id){
        return ResponseEntity.ok().body(studentService.delete(id));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable(name = "id") long id, @Valid @RequestBody StudentDTO studentDTO, BindingResult result){
        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok().body(StudentResponse.mapToResponse(studentService.update(id,Student.builder()
                .name(studentDTO.getName())
                .city(studentDTO.getCity())
                .dob(studentDTO.getDob())
                .rank(studentDTO.getRank())
                .build())));
    }

    @GetMapping("getPagination")
    public ResponseEntity<StudentListResponse> getPagination(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        PageRequest request = PageRequest.of(page,size);
        Page<Student> list = studentService.getPagination(request);
        return ResponseEntity.ok().body(new StudentListResponse(list.getTotalPages(),list.stream()
                .map(StudentDTO::mapToStudentDTO).toList()));
    }
}
