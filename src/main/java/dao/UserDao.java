package dao;

import dto.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserDao extends BasicDAO<User, ObjectId> {
    public UserDao(Datastore datastore) {
        super(datastore);
    }

    public Optional<User> getUser(ObjectId userId) {
        final User user = this.createQuery()
                .field("_id").equal(userId)
                .field("is_deleted").equal(false)
                .get();
        return Optional.ofNullable(user);
    }

    public List<User> getSpecificUsers(Set<ObjectId> userIdSet) {
        final List<User> users = this.createQuery()
                .field("_id").in(userIdSet)
                .field("is_deleted").equal(false)
                .asList();
        return users;
    }

    public void savePhoto(ObjectId userId, String file) {
        final Query<User> query = this.createQuery()
                .field("_id").equal(userId);
        final UpdateOperations<User> ops = this.createUpdateOperations();
        ops.set("picture", file);
        final UpdateResults updateResults = this.update(query, ops);
    }

    public void saveModel(ObjectId userId, List<Double> model) {
        final Query<User> query = this.createQuery()
                .field("_id").equal(userId);
        if (model == null) {
            return;
        }
        final UpdateOperations<User> ops = this.createUpdateOperations();
        ops.set("model", model);
        final UpdateResults updateResults = this.update(query, ops);
    }

    public Optional<ObjectId> createUser(User user) {
        this.save(user);
        return Optional.of(user.getId());
    }

    public Optional<User> getUserByPhone(String phone) {
        final User user = this.createQuery()
                .field("phone").equal(phone)
                .field("is_deleted").equal(false)
                .get();
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByEmail(String email) {
        final User user = this.createQuery()
                .field("email").equal(email)
                .field("is_deleted").equal(false)
                .get();
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByPhoneOrEmail(String phone, String email) {
        final Query<User> q = this.createQuery()
                .field("is_deleted").equal(false);
        q.or(
                q.criteria("phone").equal(phone),
                q.criteria("email").equal(email)
        );
        return Optional.ofNullable(q.get());
    }
}
