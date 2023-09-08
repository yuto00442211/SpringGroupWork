package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
   
    private int goods_id;

    private String name;
    
    private Integer genre_id;
    
    private int initial_price;
    
    private LocalDateTime start_time;
    
    private LocalDateTime end_time;
    
    private String image_number;
    
    private String comment;
    
}