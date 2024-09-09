package com.example.demo.responses;

import com.example.demo.dtos.StudentDTO;
import com.example.demo.models.Ranks;
import com.example.demo.models.Student;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StudentResponse extends BaseResponse{
    private String name;
    private String city;
    private LocalDate dob;
    private Ranks rank;

    public static StudentResponse mapToResponse(Student student){
        StudentResponse studentResponse = StudentResponse.builder()
                .name(student.getName())
                .city(student.getCity())
                .dob(student.getDob())
                .rank(student.getRank())
                        .build();
        studentResponse.setUpdateAt(LocalDateTime.now());
        studentResponse.setCreateAt(LocalDateTime.now());
        return studentResponse;
    }
}
