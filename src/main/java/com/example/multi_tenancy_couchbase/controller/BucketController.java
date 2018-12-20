package com.example.multi_tenancy_couchbase.controller;

import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.example.multi_tenancy_couchbase.model.BucketModel;
import com.example.multi_tenancy_couchbase.repository.BucketRepository;
import com.example.multi_tenancy_couchbase.service.BucketService;
import com.example.multi_tenancy_couchbase.service.TemplteCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bucket")
public class BucketController {


  @Autowired
  private BucketService bucketService;

  @Autowired
  private TemplteCustomer templteCustomer;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<BucketModel>> getAllBucket(){
    List<BucketModel> bucketModelList;
    bucketModelList = this.templteCustomer.findAll();
    return new ResponseEntity<>(bucketModelList, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<BucketModel> createBucket(@RequestBody BucketModel bucketModel){
    BucketModel bucketModel1 =this.bucketService.create(bucketModel);
    return new ResponseEntity<>(bucketModel1,HttpStatus.OK);
  }


  @RequestMapping(method =RequestMethod.PUT )
  public void updateBucket(@RequestBody BucketModel bucketModel){
    this.bucketService.update(bucketModel);
  }
}

