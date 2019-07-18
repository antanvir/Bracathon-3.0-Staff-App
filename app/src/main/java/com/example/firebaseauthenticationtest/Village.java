package com.example.firebaseauthenticationtest;



public class Village {
    String human;
    Double lattitude, longitude;

    public Village(){

    }

    public Village(String human, Double lattitude, Double longitude) {
        this.human = human;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getHuman() {
        return human;
    }

    public void setHuman(String human) {
        this.human = human;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
