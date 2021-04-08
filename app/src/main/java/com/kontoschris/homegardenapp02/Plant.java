package com.kontoschris.homegardenapp02;

public class Plant {
    private int id;
    private String title;
    private String description;
    private int temperature;
    private int humidity;
    byte[] image;



    public Plant(String title, String description, int temperature, int humidity, byte[] image) {
        this.title = title;
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public byte[] getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
