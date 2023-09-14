package com.example.demo.Form;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
	
    private int goods_id;
    
    private String name;
    
    private String readStatus; // 追加：既読 or 未読のステータス
    
    private Integer genre_id;
    
    private int initial_price;
    
    private LocalDateTime start_time;
    
    private LocalDateTime end_time;
    
    private String image_number;
    
    private String comment;
    
    private Integer account_id;

}
