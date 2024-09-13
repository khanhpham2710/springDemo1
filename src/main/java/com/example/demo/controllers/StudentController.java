package com.example.demo.controllers;



import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.StudentImageDTO;
import com.example.demo.models.Ranks;
import com.example.demo.models.Student;
import com.example.demo.models.StudentImage;
import com.example.demo.responses.ApiResponse;
import com.example.demo.responses.StudentListResponse;

import com.example.demo.services.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;


import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<Student> students = studentService.showAll();
        List<StudentDTO> responses = students.stream()
                .map(StudentDTO::mapToStudentDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(responses)
                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getByID(@PathVariable long id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentDTO.mapToStudentDTO(student))
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStudent(@RequestBody @Valid StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Validation failed")
                    .data(errors)
                    .build());
        }
        Student student = studentService.add(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(StudentDTO.mapToStudentDTO(student))
                .message("Student created successfully")
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable long id) {
        String message = studentService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable long id, @Valid @RequestBody StudentDTO studentDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Validation failed")
                    .data(errors)
                    .build());
        }
        Student updatedStudent = studentService.update(id, StudentDTO.mapToStudent(studentDTO));
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentDTO.mapToStudentDTO(updatedStudent))
                .message("Student updated successfully")
                .build());
    }

    @GetMapping("/pagination")
    public ResponseEntity<ApiResponse> getPagination(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page number must be non-negative") int page,
            @RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "Size must be greater than zero") int size) {
        PageRequest request = PageRequest.of(page, size);
        Page<Student> list = studentService.getPagination(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentListResponse.builder()
                        .students(list.getContent())
                        .totalPages(list.getTotalPages())
                        .build())
                .build());
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<ApiResponse> getByNameContaining(@PathVariable(name = "name") String name) {
        PageRequest request = null;
        Page<Student> students = studentService.findByNameContaining(name, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(students)
                .build());
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getByNameOrderBy(@RequestParam(name = "name", required = true) String name,
                                                        @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
                                                        @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page number must be non-negative") int page,
                                                        @RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "Size must be greater than zero") int size) {
        PageRequest request = PageRequest.of(page, size);
        Page<Student> students = studentService.findByNameContainingIgnoreCaseOrderBy(name, orderBy, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentListResponse.builder()
                        .students(students.getContent())
                        .totalPages(students.getTotalPages())
                        .build())
                .build());
    }


    @GetMapping("/search1")
    public ResponseEntity<ApiResponse> getByNameContainingIgnoreCaseDobBetweenOrderByCreateAtAsc(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "startYear", defaultValue = "0") int startYear,
            @RequestParam(name = "endYear", defaultValue = "0") Integer endYear,
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page number must be non-negative") int page,
            @RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "Size must be greater than zero") int size) {

        if (endYear == null || endYear == 0) {
            endYear = LocalDate.now().getYear();
        }

        PageRequest request = PageRequest.of(page, size);
        Page<Student> students = studentService.findByNameContainingIgnoreCaseDobBetweenOrderByCreateAtAsc(name, startYear, endYear, request);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentListResponse.builder()
                        .students(students.getContent())
                        .totalPages(students.getTotalPages())
                        .build())
                .build());
    }

    @GetMapping("/search2")
    public ResponseEntity<ApiResponse> getByRankAndNameContainingIgnoreCaseOrderByCreateAtAsc(
            @RequestParam(name = "studentRank", required = false) Ranks studentRank,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page number must be non-negative") int page,
            @RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "Size must be greater than zero") int size) {

        PageRequest request = PageRequest.of(page, size);
        Page<Student> students = studentService.findByStudentRankAndNameContainingIgnoreCaseOrderByCreateAtAsc(studentRank, name, request);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentListResponse.builder()
                        .students(students.getContent())
                        .totalPages(students.getTotalPages())
                        .build())
                .build());
    }

    @GetMapping("/searchthanthanh")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "studentRank", required = false) Ranks studentRank,
            @RequestParam(name = "startYear", defaultValue = "0") int startYear,
            @RequestParam(name = "endYear", defaultValue = "0") Integer endYear,
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Trang không được âm") int page,
            @RequestParam(name = "size", defaultValue = "5") @Min(value = 1, message = "Số học sinh 1 trang phải lớn hơn 1") int size) {

        if (endYear == null || endYear == 0) {
            endYear = LocalDate.now().getYear();
        }

        PageRequest request = PageRequest.of(page, size);
        Page<Student> students = studentService.searchThanThanh(
                name != null ? "%" + name + "%" : "%",
                studentRank,
                startYear,
                endYear,
                request);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(StudentListResponse.builder()
                        .students(students.getContent())
                        .totalPages(students.getTotalPages())
                        .build())
                .build());
    }

    @GetMapping("/getAllImages/{id}")
    public ResponseEntity<ApiResponse> getAllImages(@PathVariable(name = "id") Long studentId) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(studentService.findByStudentId(studentId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/updateImage/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateIamge(@PathVariable(name = "id") Long id,
                                                   @ModelAttribute List<MultipartFile> files) throws IOException {
        studentService.findById(id);
        List<StudentImage> images = new ArrayList<>();
        int count = 0;
        for (MultipartFile file : files){
            if(file != null){
                if (file.getSize() == 0){
                    count++;
                    continue;
                }
            }
            String fileName = storeFile(file);
            StudentImageDTO studentImageDTO = StudentImageDTO.builder()
                    .imageUrl(fileName)
                    .build();
            images.add(studentService.updateImage(id, studentImageDTO));
        }
        if (count == 1){
            throw new IllegalArgumentException("Files rổng");
        }

        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Update sucessfully")
                .data(images)
                .build();
        return ResponseEntity.ok(response);
    }

    public String storeFile(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        java.nio.file.Path uploadDdir = Paths.get("update");
        if(!Files.exists(uploadDdir)){
            Files.createDirectory(uploadDdir);
        }
        java.nio.file.Path destination = Paths.get(uploadDdir.toString(),uniqueFileName);
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
}
