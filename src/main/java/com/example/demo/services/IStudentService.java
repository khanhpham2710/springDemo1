package com.example.demo.services;

import com.example.demo.dtos.StudentDTO;
import com.example.demo.models.Student;
import com.example.demo.responses.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    public List<Student> showAll();

    public Student findById(long Id);

    public Student update(long Id,Student student);

    public String delete(long Id);

    public Page<Student> getPagination(PageRequest request);

    public Student add(StudentDTO studentDTO);
}
