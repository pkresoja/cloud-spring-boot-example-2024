package com.pequla.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "access")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Access {

    @Id
    @Column(name = "access_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
