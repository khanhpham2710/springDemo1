package com.example.demo.models;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class BaseEntity {
    @CreationTimestamp
    @Column(name="create_at",updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name="update_at")
    private LocalDateTime updateAt;
}
