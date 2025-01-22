package com.example.urs_2024_25;
//blueprint za spremanje informaija o jednom studentu
public class ListData {
    String name, surname, time;
    int image;

    public ListData( String time, int image, String name, String surname) {
        this.time = time;
        this.image = image;
        this.name = name;
        this.surname = surname;
    }
}
