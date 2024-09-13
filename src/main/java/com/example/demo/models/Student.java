package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
@Builder
public class Student extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Tên không được đẻ trống")
    private String name;

    @NotBlank(message = "Thành phố không được đẻ trống")
    private String city;

    @Column(name = "date_of_birth")
    @Past(message = "Nhập ngày sinh ở quá khứ")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Xếp loại không được để trống")
    @Column(name = "student_rank")
    private Ranks studentRank;
}
