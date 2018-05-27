package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModel {
    private Integer number;
    private Integer option;

    @JsonCreator
    public ResponseModel(
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
