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
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    private ObjectId userId;
    private ObjectId testId;
    private List<Response> responses;

    @JsonCreator
    public Question(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("userId") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId userId,
            @JsonProperty("testId") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId testId,
            @JsonProperty("registration") List<Response> responses
    ) {
        this.id = id;
        this.userId = userId;
        this.testId = testId;
        this.responses = responses;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public ObjectId getTestId() {
        return testId;
    }

    public List<Response> getResponses() {
        return responses;
    }
}
