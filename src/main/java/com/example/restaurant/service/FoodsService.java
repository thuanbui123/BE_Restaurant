package com.example.restaurant.service;

import com.example.restaurant.entity.FoodsEntity;
import com.example.restaurant.repository.FoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodsService {
    @Autowired
    private FoodsRepository foodsRepository;

    public FoodsEntity findOneById (Integer id) {
        return foodsRepository.findOneById(id);
    }
}
