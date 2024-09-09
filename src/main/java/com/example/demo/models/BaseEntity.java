package com.example.demo.models;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @CreationTimestamp
    @Column(name="create_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name="update_at")
    private LocalDateTime updateAt;
}

