package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bit {

    @Id
    private Goods goods;

    
    private Account account;

  
    private LocalDateTime bidTime;

    private BigDecimal currentPrice;

    // Getters and setters
}