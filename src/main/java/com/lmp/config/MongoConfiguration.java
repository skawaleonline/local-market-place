//package com.lmp.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.convert.CustomConversions;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//import com.mongodb.MongoClient;
//
//@Configuration
//@EnableMongoRepositories(basePackages = {"com.lmp.db.repository"})
//public class MongoConfiguration extends AbstractMongoConfiguration {
//
//  @Value("${mongo.dbname}")
//  private String databaseName;
//
//  @Value("${mongo.username}")
//  private String username;
//  
//  @Value("${mongo.password}")
//  private String password;
//
//  @Value("${mongo.host}")
//  private String host;
//
//  @Value("${mongo.port}")
//  private int port;
//
//  @Bean
//  public MongoTemplate mongoTemplate() throws Exception {
//    return new MongoTemplate(mongoDbFactory());
//  }
//
//  @Override
//  public MongoClient mongoClient() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  protected String getDatabaseName() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public CustomConversions customConversions() {
//      return new CustomConversions(GeoJsonConverters.getConvertersToRegister());
//  }
//}