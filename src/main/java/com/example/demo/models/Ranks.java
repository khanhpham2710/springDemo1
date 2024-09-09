package com.example.demo.models;

public enum Ranks {
    GIOI("Giỏi"),KHA("Khá"),TRUNGBINH("Trung bình"),YEU("Yếu");

    private String rank;

    Ranks(String s){
        this.rank = s;
    };

    public String getRank(){
        return this.rank;
    }
}
