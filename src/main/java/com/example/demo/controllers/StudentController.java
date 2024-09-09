package com.example.demo.controllers;


import com.example.demo.dtos.StudentDTO;
import com.example.demo.models.Student;
import com.example.demo.responses.StudentResponse;
import com.example.demo.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/student")

public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/getAll")
    public ResponseEntity<List<StudentResponse>> getAll() {
        List<StudentResponse> list = studentService.showAll().stream()
                .map(StudentResponse::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getByID(@PathVariable(name = "id") long id) {
        Student student = studentService.findById(id);
        if (student == null){
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok().body(StudentResponse.mapToResponse(student));
    }

    @PostMapping("add")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok().body(StudentResponse.mapToResponse(studentService.add(studentDTO));
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
    public ResponseEntity<List<StudentResponse>> getPagination(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        PageRequest request = PageRequest.of(page,size);
        List<StudentResponse> list = studentService.getPagination(request).stream()
                .map(StudentResponse::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }
}
