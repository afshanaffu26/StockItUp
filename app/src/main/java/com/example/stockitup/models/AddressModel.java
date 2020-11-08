package com.example.stockitup.models;

/**
 * Model file for Address
 */
public class AddressModel {
    private String name;
    private String addressLine;
    private String city;
    private String province;
    private String country;
    private String pincode;
    private String phone;

    /**
     * Non - parameterized constructor
     */
    public AddressModel() {
    }

    /**
     * Constructor to initialize name, addressLine, city, province, country, pincode, phone
     * @param name user name
     * @param addressLine user addressLine
     * @param city user city
     * @param province user province
     * @param country user country
     * @param pincode user pincode
     * @param phone user phone
     */
    public AddressModel(String name, String addressLine, String city, String province, String country, String pincode, String phone) {
        this.name = name;
        this.addressLine = addressLine;
        this.city = city;
        this.province = province;
        this.country = country;
        this.pincode = pincode;
        this.phone = phone;
    }

    /**
     * Getter method to get name of address
     * @return Name of address
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method to set the name for address
     * @param name Name to be set to address
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to get addressLine of address
     * @return addressLine of address
     */
    public String getAddressLine() {
        return addressLine;
    }

    /**
     * Setter method to set the addressLine for address
     * @param addressLine the addressLine to be set to address
     */
    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    /**
     * Getter method to get city of address
     * @return city of address
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter method to set the city for address
     * @param city the city to be set to address
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter method to get province of address
     * @return province of address
     */
    public String getProvince() {
        return province;
    }

    /**
     * Setter method to set the province for address
     * @param province the province to be set to address
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Getter method to get country of address
     * @return country of address
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter method to set the country for address
     * @param country the country to be set to address
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter method to get pincode of address
     * @return pincode of address
     */
    public String getPincode() {
        return pincode;
    }

    /**
     * Setter method to set the pincode for address
     * @param pincode the pincode to be set to address
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     * Getter method to get phone of address
     * @return phone of address
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter method to set the phone for address
     * @param phone the phone to be set to address
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
