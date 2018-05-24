package dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jackson.JsonSnakeCase;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;


/**
 * Used to login at the time of examination.
 */
@JsonSnakeCase
public class UserDetailed {
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId userId;
    private String userName;
    private String registration;
    private String password;
    private String photo;

    @JsonCreator
    public UserDetailed(@JsonSerialize(using = ObjectIdSerializer.class) ObjectId userId,
                       @JsonProperty("userName") String userName,
                        @JsonProperty("registration") String registration,
                       @JsonProperty("password") String password,
                       @JsonProperty("photo") String photo
                       ) {
        this.userId = userId;
        this.userName = userName;
        this.registration = registration;
        this.password = password;
        this.photo = photo;
    }

    public String getRegistration() {
        return registration;
    }

    public String getPassword() {
        return password;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoto() {
        return photo;
    }
}
