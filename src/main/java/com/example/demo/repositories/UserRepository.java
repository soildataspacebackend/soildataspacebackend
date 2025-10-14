package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface UserRepository extends MongoRepository<User, String> {
    ArrayList<User> findAllBy();

    User getUserByEmail(String email);

    User getUserByName(String name);

    User findByAuthToken(String authToken);
}
