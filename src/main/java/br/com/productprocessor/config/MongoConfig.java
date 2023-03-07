package br.com.productprocessor.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig implements CommandLineRunner {

    @Autowired
    private MongoClient mongoClient;

    @Override
	public void run(String... args) throws Exception {
		mongoClient.getDatabase("productDatabase").getCollection("product")
				.createIndex(Indexes.compoundIndex(Indexes.ascending("product"), Indexes.ascending("quantity"),
						Indexes.ascending("price"), Indexes.ascending("type"), Indexes.ascending("industry"),
						Indexes.ascending("origin")), new IndexOptions().unique(true));
	}
}
