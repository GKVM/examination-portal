package dao;

import dto.Test;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;

import java.util.List;
import java.util.Optional;

public class ExaminationDao extends BasicDAO<Test, ObjectId> {
    public ExaminationDao(Datastore datastore) {
        super(datastore);
    }

    public Optional<Test> getTest(ObjectId testId) {
        final Test questions = this.createQuery()
                .field("_id").equal(testId)
                .get();
        return Optional.ofNullable(questions);
    }

//    public List<Test> allTestGet() {
//        List<Test> allTests =  new ArrayList<>();//this.createQuery().get();
//        return allTests;
//    }

    public void addExam(Test test){
        this.save(test);
    }

    public List<Test> listExams() {
        List<Test> exams = this.createQuery().asList(new FindOptions().limit(30));
        return exams;
    }
}
