package org.total.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

/**
 * @author Pavlo.Fandych
 */

@Configuration
@EnableMongoRepositories(basePackages = "org.total.repository")
public class MongoConfig extends AbstractMongoConfiguration {

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(Collections.singletonList(new ServerAddress("172.22.194.40", 27017)),
                MongoCredential.createScramSha1Credential("root", "admin", "Avid123".toCharArray()),
                MongoClientOptions.builder().writeConcern(WriteConcern.JOURNALED).build());
    }

    @Override
    protected String getDatabaseName() {
        return "demo";
    }
}
