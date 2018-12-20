package com.example.multi_tenancy_couchbase.service;


import com.example.multi_tenancy_couchbase.model.BucketModel;
import com.example.multi_tenancy_couchbase.repository.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BucketService {

  @Autowired
  private BucketRepository bucketRepository;


  public List<BucketModel> findAll(){
    List<BucketModel> bucketModelList =new ArrayList<>();
    Iterator<BucketModel> bucket=this.bucketRepository.findAll().iterator();
    while (bucket.hasNext()){
     bucketModelList.add(bucket.next());
    }
    return bucketModelList;
  }

 public BucketModel create(BucketModel bucketModel){
   BucketModel bucketModel1 =this.bucketRepository.save(bucketModel);
   return bucketModel1;
  }

  public BucketModel update(BucketModel bucketModel){
    BucketModel bucketModelUpdate =this.bucketRepository.save(bucketModel);
    return bucketModelUpdate;
  }

  public void delete(BucketModel bucketModel){
   this.bucketRepository.delete(bucketModel);
  }
}
