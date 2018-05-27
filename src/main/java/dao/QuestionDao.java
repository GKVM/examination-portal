package dao;

import dto.Questions;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Optional;

public class QuestionDao extends BasicDAO<Questions, ObjectId> {
    public QuestionDao(Datastore datastore) {
        super(datastore);
    }

    public Optional<Questions> getQuestionSet(ObjectId testId) {
        final Questions questions = this.createQuery()
                .field("testId").equal(testId)
                .get();
        return Optional.ofNullable(questions);
    }
}
