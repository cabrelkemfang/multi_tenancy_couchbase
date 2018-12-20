package com.example.multi_tenancy_couchbase.service;

import com.couchbase.client.java.view.ViewQuery;
import com.example.multi_tenancy_couchbase.model.BucketModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("TemplteCustomer")
public class TemplteCustomer {

  private static final String DESIGN_DOC = "bucketModel";

  private CouchbaseTemplate template;

  @Autowired
  public void setCouchbaseTemplate(CouchbaseTemplate template) {
    this.template = template;
  }

  public BucketModel findOne(String id){
    return template.findById(id, BucketModel.class);
  }

  public List<BucketModel> findAll(){
    return template.findByView(ViewQuery.from(DESIGN_DOC,"all"), BucketModel.class);
  }

  public  List<BucketModel> findByLastName(String lastName){
    return template.findByView((ViewQuery.from(DESIGN_DOC,"byLastName")), BucketModel.class);
  }

  public void create(BucketModel bucketModel){
    template.insert(bucketModel);
  }

  public void update(BucketModel bucketModel){
    template.update(bucketModel);
  }
}
