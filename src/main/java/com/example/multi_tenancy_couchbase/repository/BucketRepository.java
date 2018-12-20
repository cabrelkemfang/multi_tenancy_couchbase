package com.example.multi_tenancy_couchbase.repository;

import com.example.multi_tenancy_couchbase.model.BucketModel;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.repository.CrudRepository;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "bucketModel")
public interface BucketRepository extends CrudRepository<BucketModel,String> {

}
