package dao;

import dto.Responses;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Optional;

public class ResponseDao extends BasicDAO<Responses, ObjectId> {
    public ResponseDao(Datastore datastore) {
        super(datastore);
    }

    public Optional<ObjectId> saveResponse(Responses responses) {
        this.save(responses);
        return Optional.of(responses.getId());
    }

    public Responses fetchResponsesForQuestionSet(ObjectId testId, ObjectId candidateId) {
        Responses responses = this.createQuery()
                .field("testId").equal(testId)
                .field("userId").equal(candidateId)
                .get();
        return responses;
    }

    public Responses updateResponse(ObjectId candidateId, ObjectId testId) {
        Query<Responses> q = this.createQuery()
                .field("testId").equal(testId)
                .field("userId").equal(candidateId);
        UpdateOperations<Responses> u = this.createUpdateOperations()
                .push("response", "");
        return null;
    }
}
