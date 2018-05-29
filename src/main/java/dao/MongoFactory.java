package dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import config.ExamConfiguration;

public class MongoFactory {
    public final MongoClient build(final ExamConfiguration configuration) {
        final MongoClientURI mongoClientURI = /*System.getenv("DATABASE").isEmpty() ?*/
                new MongoClientURI(configuration.getMongoDbUri()) /*:
                new MongoClientURI(System.getenv("DATABASE"))*/;

        return new MongoClient(mongoClientURI);
    }
}
