package com.example.stockitup.models;

public class AddressModel {

    private String name;
    private String addressLine;
    private String city;
    private String province;
    private String country;
    private String pincode;
    private String phone;

    public AddressModel() {
    }

    public AddressModel(String name, String addressLine, String city, String province, String country, String pincode, String phone) {
        this.name = name;
        this.addressLine = addressLine;
        this.city = city;
        this.province = province;
        this.country = country;
        this.pincode = pincode;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
