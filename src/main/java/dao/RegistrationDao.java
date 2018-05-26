package dao;

import dto.Registration;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;
import java.util.Optional;

public class RegistrationDao extends BasicDAO<Registration, ObjectId> {
    public RegistrationDao(Datastore datastore) {
        super(datastore);
    }

    public Boolean createEntry(Registration registration) {
        this.save(registration);
        return Boolean.TRUE;
    }

    public Optional<Registration> getRegistration(String registration){
        Registration registrationInfo = this.createQuery()
                .get();
        // TODO: 26/5/18
        return null;
    }

    public List<Registration> getAllRegistrationForTest(ObjectId testId) {
        List<Registration> registrations = this.createQuery()
                .field("exam_id").equal(testId)
                .asList();
        return registrations;
    }

    public Boolean checkIfRegistrationExists(ObjectId userId, ObjectId testId){
        List<Registration> registrations = this.createQuery()
                .field("user_id").equal(userId)
                .field("exam_id").equal(testId)
                .asList();
        return registrations.isEmpty();
    }
}
