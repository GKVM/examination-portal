package dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jackson.JsonSnakeCase;
import json.ObjectIdDeserializer;
import json.ObjectIdSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;

@JsonSnakeCase
public class JwtPayload {

    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId userId;
    private String requestSource;

    @JsonCreator
    public JwtPayload(
            @JsonProperty("user_id") @JsonSerialize(using = ObjectIdSerializer.class) ObjectId userId
    ) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(userId)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JwtPayload)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        JwtPayload other = (JwtPayload) obj;
        return new EqualsBuilder()
                .append(userId, other.userId)
                .build();
    }

    public ObjectId getUserId() {
        return userId;
    }
}
