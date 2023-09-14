package com.example.demo.Form;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitinfoDTO{
    @Id
    private int goods_id;
    private int account_id;
    private LocalDateTime bid_time;
    private int current_price;
    private boolean notlook;
    private boolean timeup;
    private String name;

    // ゲッターとセッター
}
