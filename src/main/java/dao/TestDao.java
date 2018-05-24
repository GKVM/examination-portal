package dao;

import dto.Test;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;
import java.util.Optional;

public class TestDao extends BasicDAO<Test, ObjectId> {
    public TestDao(Datastore datastore) {
        super(datastore);
    }

    public Optional<Test> getTest(ObjectId testId) {
        final Test questions = this.createQuery()
                .field("_id").equal(testId)
                .get();
        return Optional.ofNullable(questions);
    }

    public List<Test> allTest() {
        final List<Test> tests = this.createQuery()
                .asList();
        return tests;
    }
}
