package dao;

import dto.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Optional;

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

    public Optional<User> loginUser(String phone, String email) {
        final Query<User> q = this.createQuery()
                .field("is_deleted").equal(false);
        q.or(
                q.criteria("phone").equal(phone),
                q.criteria("email").equal(email)
        );
        User user = q.get();
        final UpdateOperations<User> update = this.createUpdateOperations();
        update.set("last_seen", System.currentTimeMillis());
        this.updateFirst(q, update);
        return Optional.ofNullable(user);
    }
}
