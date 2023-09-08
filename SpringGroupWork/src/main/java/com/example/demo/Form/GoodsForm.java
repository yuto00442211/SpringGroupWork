package com.example.demo.Form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class GoodsForm {
	
	    @NotBlank(message = "商品名を入力してください")
	    private String name;
	    
	    private Integer genre_id;

	    @DecimalMin("0")
	    private Integer initial_price;

	    @Future(message = "終了時間を入力してください")
	    private LocalDateTime endtime;
	    
//	    private  MultipartFile imagenumber;

	    @NotBlank(message = "眠眠打破を入力してください")
	    private String comment;

	}

