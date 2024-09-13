package com.example.demo.responses;

import com.example.demo.dtos.StudentDTO;
import com.example.demo.models.Student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentListResponse{
    private int totalPages;
    private List<Student> students;
}
