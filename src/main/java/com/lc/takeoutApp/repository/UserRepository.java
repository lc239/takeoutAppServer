package com.lc.takeoutApp.repository;

import com.lc.takeoutApp.pojo.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findByPhone(String phone);
    @Query("update user set username=:username where id=:id")
    Mono<Boolean> updateUsernameById(@Param("username") String username, @Param("id") Long id);
}
