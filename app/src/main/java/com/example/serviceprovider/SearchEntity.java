package com.example.serviceprovider;



public class SearchEntity {
    private String title;
    private String address;
    private String lattitude;
    private String longtitude;

    public SearchEntity(String title, String address) {
        this.title = title;
        this.address = address;
    }
    public SearchEntity(String title, String address,String lat, String longti) {
        this.title = title;
        this.address = address;
        this.lattitude = lat;
        this.longtitude = longti;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }
}
