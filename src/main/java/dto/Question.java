package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

public class Question {
    private Integer num;
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;

    @JsonCreator
    public Question(
            @JsonProperty("num") Integer num,
            @JsonProperty("question") String question,
            @JsonProperty("optiona") String optiona,
            @JsonProperty("optionb") String optionb,
            @JsonProperty("optionc") String optionc,
            @JsonProperty("optiond") String optiond
    ) {
        this.num = num;
        this.question = question;
        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
    }

    public Integer getNum() {
        return num;
    }
}
