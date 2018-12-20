package com.example.multi_tenancy_couchbase.configuration;

import com.couchbase.client.java.Bucket;

import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;

import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;

import com.example.multi_tenancy_couchbase.model.BucketModel;
import com.example.multi_tenancy_couchbase.model.Employee;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.json.JSONObject;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

  //@Autowired
  //private BucketRepository bucketRepository;

  @Value("${couchbase.bootstrap-hosts}")
  private String host;

  @Value("${couchbase.bucket.name}")
  private String bucketName;

  @Value("${couchbase.bucket.password}")
  private String password;

  @Value("${couchbase.bucket.admin-user}")
  private String adminUser;


  @Override
  protected List<String> getBootstrapHosts() {
    return Arrays.asList(this.host);
  }

  @Override
  protected String getBucketName() {
    return this.bucketName;
  }

  @Override
  protected String getBucketPassword() {
    return this.password;
  }

  @Override
  public String typeKey() {
    return MappingCouchbaseConverter.TYPEKEY_SYNCGATEWAY_COMPATIBLE;
  }


  @Override
  protected String getUsername() {
    return this.adminUser;
  }

  @Override
  protected CouchbaseEnvironment getEnvironment() {
    DefaultCouchbaseEnvironment.builder().connectTimeout(60000) // by default 5 sec (5000 ms)
            .queryTimeout(20000) // by default 75 sec (75000 ms)
            .socketConnectTimeout(45000); // by default 1 sec (1000 ms)
    return super.getEnvironment();
  }

  @Bean
  public void createBucket() throws Exception {

    Bucket bucket = couchbaseCluster().openBucket("sandbox");

    //query the bucket to extract all the other bucket
    N1qlQueryResult result = bucket.query(N1qlQuery.simple("SELECT * FROM `sandbox`"));

    ClusterManager clusterManager = couchbaseCluster().clusterManager("Administrator", "admin123");

    List<BucketSettings> values = clusterManager.getBuckets();
    for (N1qlQueryRow row : result) {

      Object rowJson = row.value().get("sandbox");
      String rowString = String.valueOf(rowJson);

      JSONObject jsonObj = new JSONObject(rowString);
      String password = jsonObj.getString("password");
      String name=jsonObj.getString("bucketName");

     /* for(BucketSettings value: values){
        if (value.name()!=name){
          System.out.println(value);
          System.out.println(value.name());*/
          BucketSettings sampleBucket = new DefaultBucketSettings.Builder()
                  .type(BucketType.COUCHBASE)
                  .name(name)
                  .password(password)
                  .quota(200) // megabytes
                  .replicas(1)
                  .indexReplicas(true)
                  .enableFlush(true)
                  .build();
          clusterManager.insertBucket(sampleBucket);
        /*}
        else {

        }
      }*/
    }
  }

  @Bean
  public Bucket templateBucket() throws Exception {
    this.createBucket();
    return couchbaseCluster().openBucket("crimson");
  }

  @Bean
  public CouchbaseTemplate template() throws Exception {
    CouchbaseTemplate template = new CouchbaseTemplate(
            couchbaseClusterInfo(), templateBucket(),
            mappingCouchbaseConverter(), translationService());
    template.setDefaultConsistency(getDefaultConsistency());
    return template;
  }

  @Override
  public void configureRepositoryOperationsMapping(
          RepositoryOperationsMapping baseMapping) {
    try {
      baseMapping.mapEntity(Employee.class, template());
    } catch (Exception e) {
      throw new RuntimeException("Place bucket could not be configured properly!");
      //custom Exception handling
    }
  }



/*
  @Bean
  public Bucket testBucket() throws Exception {
    return couchbaseCluster().openBucket("test");
  }

  @Bean
  public CouchbaseTemplate campusTemplate() throws Exception {
    CouchbaseTemplate template = new CouchbaseTemplate(
            couchbaseClusterInfo(), testBucket(),
            mappingCouchbaseConverter(), translationService());
    template.setDefaultConsistency(getDefaultConsistency());
    return template;
  }

  @Bean(name = BeanNames.COUCHBASE_OPERATIONS_MAPPING)
  public RepositoryOperationsMapping repositoryOperationsMapping(CouchbaseTemplate couchbaseTemplate) throws  Exception {
    //create a base mapping that associates all repositories to the default template
    RepositoryOperationsMapping baseMapping = new RepositoryOperationsMapping(couchbaseTemplate);
    //let the user tune it
    configureRepositoryOperationsMapping(baseMapping);
    return baseMapping;
  }


  @Bean
  public CouchbaseTemplate employeeTemplate() throws Exception {
    CouchbaseTemplate template = new CouchbaseTemplate(couchbaseClusterInfo(), templateBucket(),
            mappingCouchbaseConverter(), translationService());
    template.setDefaultConsistency(getDefaultConsistency());
    return template;
  }

  @Override
  public void configureRepositoryOperationsMapping(
          RepositoryOperationsMapping baseMapping) {
    try {
      baseMapping.mapEntity(Employee.class, employeeTemplate());
    } catch (Exception e) {
      throw new RuntimeException("Place bucket could not be configured properly!");
      //custom Exception handling
    }
  }*/
}
