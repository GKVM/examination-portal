package dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jackson.JsonSnakeCase;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;


/**
 * Used to login at the time of examination.
 */
@JsonSnakeCase
public class ExamLoginRequest {
    private String registration;
    private String password;

    @JsonCreator
    public ExamLoginRequest(
            @JsonProperty("registration") String registration,
            @JsonProperty("password") String password
    ) {
        this.registration = registration;
        this.password = password;
    }

    public String getRegistration() {
        return registration;
    }

    public String getPassword() {
        return password;
    }
}
