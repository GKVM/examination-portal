package dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import config.ExamConfiguration;

public class MongoFactory {
    public final MongoClient build(final ExamConfiguration configuration) {
        final MongoClientURI mongoClientURI = new MongoClientURI(configuration.getMongoDbUri());

        return new MongoClient(mongoClientURI);
    }
}
