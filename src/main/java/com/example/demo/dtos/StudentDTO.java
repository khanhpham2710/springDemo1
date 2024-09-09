package com.example.demo.dtos;

import com.example.demo.models.Ranks;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
    @NotNull(message = "Tên không được đẻ trống")
    private String name;

    @NotNull(message = "Tên không được đẻ trống")
    private String city;

    @Column(name = "date_of_birth")
    @Past(message = "Nhập ngày sinh ở quá khứ")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Xếp loại không được để trống")
    private Ranks rank;
}
