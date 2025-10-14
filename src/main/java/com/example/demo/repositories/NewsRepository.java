package com.example.demo.repositories;

import com.example.demo.models.News;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface NewsRepository extends MongoRepository<News, String> {
    // Primera prueba
    ArrayList<News> findAllBy();

    News getNewsById(String id);
}
