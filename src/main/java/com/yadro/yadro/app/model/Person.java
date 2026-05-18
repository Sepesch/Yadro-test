package com.yadro.yadro.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long id;
    @JsonProperty("Gender")     private String gender;
    @JsonProperty("FirstName")  private String firstName;
    @JsonProperty("LastName")   private String lastName;
    @JsonProperty("Phone")      private String phone;
    @JsonProperty("Email")      private String email;
    @JsonProperty("City")       private String city;
    @JsonProperty("Street")     private String street;
    @JsonProperty("House")      private Integer house;
    private String address;
    public void prepareAddress() {
        if (city != null && street != null && house != null) {
            this.address = city + ", " + street + ", д. " + house;
        } else if (city != null) {
            this.address = city;
        } else {
            this.address = "";
        }
    }
}