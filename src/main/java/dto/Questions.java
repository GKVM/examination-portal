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

import java.util.List;

@JsonSnakeCase
@Entity(value = "test", noClassnameStored = true)
public class Questions {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;
    private ObjectId testId;
    private List<Question> questionList;

    @JsonCreator
    public Questions(
            @JsonProperty("id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId id,
            @JsonProperty("testId") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId testId,
            @JsonProperty("questionList") List<Question> questionList
    ) {
        this.id = id;
        this.testId = testId;
        this.questionList = questionList;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getTestId() {
        return testId;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
}
