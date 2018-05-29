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
import org.mongodb.morphia.annotations.NotSaved;


/**
 * Created by gkvm on 5/27/17.
 */
@JsonSnakeCase
@Entity(value = "exam", noClassnameStored = true)
public class Test {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    private String name;
    @NotSaved
    private Boolean hasApplied;
    @NotSaved
    private String number;
    private Boolean isDeleted;

    public Test() {
    }

    @JsonCreator
    public Test(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("name") String name,
            @JsonProperty("number") String number
    ) {
        this.id = id;
        this.name = name;
        this.hasApplied = false;
        this.number = number;
        this.isDeleted = false;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getHasApplied() {
        if(hasApplied == null) return false;
        return hasApplied;
    }

    public void setHasApplied(Boolean hasApplied) {
        this.hasApplied = hasApplied;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
