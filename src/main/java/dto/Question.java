package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
@JsonSnakeCase
public class Question {
    private Integer number;
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;

    public Question(){}

    @JsonCreator
    public Question(
            @JsonProperty("number") Integer number,
            @JsonProperty("question") String question,
            @JsonProperty("optiona") String optiona,
            @JsonProperty("optionb") String optionb,
            @JsonProperty("optionc") String optionc,
            @JsonProperty("optiond") String optiond
    ) {
        this.number = number;
        this.question = question;
        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
    }

    public Integer getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptiona() {
        return optiona;
    }

    public String getOptionb() {
        return optionb;
    }

    public String getOptionc() {
        return optionc;
    }

    public String getOptiond() {
        return optiond;
    }
}
