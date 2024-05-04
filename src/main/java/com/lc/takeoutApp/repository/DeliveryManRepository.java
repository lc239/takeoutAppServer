package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.DeliveryMan;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends R2dbcRepository<DeliveryMan, Long> {

}
