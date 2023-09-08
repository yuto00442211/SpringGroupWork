package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bit_Info {
    @Id
    private int goods_id;
    private int account_id;
    private LocalDateTime bid_time;
    private int current_price;

    // ゲッターとセッター
}
