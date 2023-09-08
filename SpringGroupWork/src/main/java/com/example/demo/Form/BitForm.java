package com.example.demo.Form;

import jakarta.validation.constraints.Min;

public class BitForm {
    private int productId;
    @Min(value = 1, message = "入札額は1以上で入力してください")
    private int bidAmount;

    // getters and setters
    // getters and setters
    public int getProductId() {
        return productId;
    }
    
    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }
}
