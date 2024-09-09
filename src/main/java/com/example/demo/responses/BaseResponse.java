package com.example.demo.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseResponse {
    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("update_at")
    private LocalDateTime updateAt;
}
