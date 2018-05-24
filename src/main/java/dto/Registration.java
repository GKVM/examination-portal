package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

public class Registration {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    @Property("exam_id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId testId;
    @Property("user_id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId userId;
    private String registration;
    private String password;

    Registration() {
    }

    @JsonCreator
    public Registration(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("exam_id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId testId,
            @JsonProperty("user_id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId userId,
            @JsonProperty("registration") @JsonSerialize(using = ObjectIdSerializer.class) String registration,
            @JsonProperty("password") @JsonSerialize(using = ObjectIdSerializer.class) String password
    ) {
        this.id = id;
        this.testId = testId;
        this.userId = userId;
        this.registration = registration;
        this.password = password;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public String getRegistration() {
        return registration;
    }

    public String getPassword() {
        return password;
    }

    public ObjectId getTestId() {
        return testId;
    }
}
