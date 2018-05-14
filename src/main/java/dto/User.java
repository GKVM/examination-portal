/*
 * Copyright (c) 2017. Timeline. (http://www.tline.xyz) Gopikrishna V.M.
 */

package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jackson.JsonSnakeCase;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.security.Principal;

/**
 * Created by gkvm on 5/27/17.
 */
@JsonSnakeCase
@Entity(value = "user", noClassnameStored = true)
public class User implements Principal {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String picture;
    private String password;
    @Property("is_deleted")
    private Boolean isDeleted;

    User() {
    }

    @Override
    public String getName() {
        return null;
    }

    @JsonCreator
    public User(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("name_first") String firstName,
            @JsonProperty("name_last") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("picture") String picture,
            @JsonProperty("password") String password
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.picture = picture;
        this.isDeleted = false;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
