package dao;

import dto.Responses;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Optional;

public class ResponseDao extends BasicDAO<Responses, ObjectId> {
    public ResponseDao(Datastore datastore) {
        super(datastore);
    }

    public Optional<ObjectId> saveResponse(Responses responses){
        this.save(responses);
        return Optional.of(responses.getId());
    }

    public void fetchResponsesForQuestionSet(){

    }
}
