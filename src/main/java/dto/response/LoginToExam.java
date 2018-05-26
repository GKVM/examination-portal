package dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jackson.JsonSnakeCase;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;

@JsonSnakeCase
public class LoginToExam{

    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    private String name;
    private String email;
    private String phone;
    private String token;
    private String testName;

    @JsonCreator
    public LoginToExam(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("token") String token,
            @JsonProperty("testName") String testName
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.testName = testName;
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getTestName() {
        return testName;
    }
}