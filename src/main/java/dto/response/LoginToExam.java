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
public class LoginToExam {
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId userId;
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId questionId;
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId testId;
    private String name;
    private String email;
    private String phone;
    private String token;
    private String testName;

    @JsonCreator
    public LoginToExam(
            @JsonProperty("userId") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId userId,
            @JsonProperty("testId") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId testId,
            @JsonProperty("questionId") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId questionId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("token") String token,
            @JsonProperty("testName") String testName
    ) {
        this.userId = userId;
        this.questionId = questionId;
        this.testId = testId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.testName = testName;
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

    public ObjectId getUserId() {
        return userId;
    }

    public ObjectId getQuestionId() {
        return questionId;
    }

    public ObjectId getTestId() {
        return testId;
    }

    public String getName() {
        return name;
    }
}
