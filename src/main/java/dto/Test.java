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
import java.util.Date;

/**
 * Created by gkvm on 5/27/17.
 */
@JsonSnakeCase
@Entity(value = "test", noClassnameStored = true)
public class Test implements Principal {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    private String name;
    private Date test;
    @Property("is_deleted")
    private Boolean isDeleted;

    Test() {
    }

    @JsonCreator
    public Test(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("name") String name,
            @JsonProperty("test") Date test
    ) {
        this.id = id;
        this.name = name;
        this.isDeleted = false;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
