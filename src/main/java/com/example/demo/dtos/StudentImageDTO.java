package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentImageDTO {
    @JsonProperty("student_id")
    @Min(value = 1,message = "Student ID must be greater than 0")
    private Long studentId;

    @JsonProperty("image_url")
    @Size(min = 3,max = 300, message = "Url must be between 3 and 300 characters")
    private String imageUrl;
}
