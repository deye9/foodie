package com.foodie;

public class FoodieBaseResponse {

    private Object data;

    public FoodieBaseResponse(Object data) {
        this.data = data;
    }

    public Object getdata() {
        return this.data;
    }

    public void setdata(Object data) {
        this.data = data;
    }
}
