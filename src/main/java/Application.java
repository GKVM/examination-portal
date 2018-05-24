import auth.Authenticator;
import auth.JWTAuthFilter;
import com.mongodb.MongoClient;
import config.ExamConfiguration;
import dao.MongoFactory;
import dao.UserDao;
import database.MongoClientManager;
import dto.User;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import resource.CandidateResource;
import resource.PathResource;
import service.UserService;

import java.util.Map;

public class Application extends io.dropwizard.Application<ExamConfiguration> {
    public static void main(String[] args) throws Exception {
        new Application().run(args);
    }

    @Override
    public String getName() {
        return "web-api";
    }

    @Override
    public void initialize(Bootstrap<ExamConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new ViewBundle<ExamConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(ExamConfiguration config) {
                return config.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(ExamConfiguration configuration,
                    Environment environment) {

        final MongoFactory mongoFactory = new MongoFactory();
        final MongoClient mongoClient = mongoFactory.build(configuration);
        final MongoClientManager mongoClientManager = new MongoClientManager(mongoClient);

        final Morphia morphia = new Morphia();
        morphia.mapPackage("dao");
        final Datastore datastore = morphia.createDatastore(mongoClient, configuration.getMongoDbDatabase());
        datastore.ensureIndexes();

        //Initialize DAOs
        final UserDao userDao = new UserDao(datastore);

        final UserService userService = new UserService(userDao, configuration.getSecret());


        final CandidateResource candidateResource = new CandidateResource(userService);
        final PathResource pathResource = new PathResource();

        //Initialize Resources
        environment.jersey().register(new AuthDynamicFeature(
                new JWTAuthFilter.Builder<User>()
                        .setAuthenticator(new Authenticator(configuration.getSecret(), userDao))
                        .setPrefix("Bearer")
                        .buildAuthFilter()
        ));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(candidateResource);
        environment.jersey().register(pathResource);
        environment.jersey().setUrlPattern("/api");
        //environment.jersey().setUrlPattern("/");
        //environment.healthChecks().register("mongodb connection", new MongoHealthCheck(mongoClient));

//        environment.lifecycle().manage(mongoClientManager);
//        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("crossOriginRequests", CrossOriginFilter.class);
//        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
//        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
//        cors.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
//        cors.setInitParameter("allowCredentials", "true");
//        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
