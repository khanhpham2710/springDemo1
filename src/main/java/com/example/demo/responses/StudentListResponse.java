package com.example.demo.responses;

import com.example.demo.models.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentListResponse {
    private int totalPages;
    private List<Student> students;
}
