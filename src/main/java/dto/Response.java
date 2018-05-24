package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class Response {
    private Integer number;
    private Integer option;

    @JsonCreator
    public Response(
            @JsonProperty("number") Integer number,
            @JsonProperty("option") Integer option
    ){
        this.number = number;
        this.option = option;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getOption() {
        return option;
    }
}
