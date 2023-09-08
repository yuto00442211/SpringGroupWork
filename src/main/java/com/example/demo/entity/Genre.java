package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Genre {
    @Id
    private Integer genre_id;

    private String genreName;
}