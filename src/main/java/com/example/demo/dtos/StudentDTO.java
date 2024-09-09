package com.example.demo.dtos;

import com.example.demo.models.Ranks;
import com.example.demo.models.Student;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    @NotBlank(message = "Tên không được đẻ trống")
    private String name;

    @NotBlank(message = "Thành phố không được đẻ trống")
    private String city;

    @Column(name = "date_of_birth")
    @Past(message = "Nhập ngày sinh ở quá khứ")
    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Xếp loại không được để trống")
    private Ranks rank;

    public static Student mapToStudent(StudentDTO studentDTO){
        return Student.builder()
                .name(studentDTO.getName())
                .city(studentDTO.getCity())
                .dob(studentDTO.getDob())
                .rank(studentDTO.getRank())
                .build();
    }

    public static StudentDTO mapToStudentDTO(Student student){
        return StudentDTO.builder()
                .name(student.getName())
                .city(student.getCity())
                .dob(student.getDob())
                .rank(student.getRank())
                .build();
    }
}
