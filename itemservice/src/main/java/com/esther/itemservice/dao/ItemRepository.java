package com.esther.itemservice.dao;

import com.esther.itemservice.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ItemRepository extends MongoRepository<Item, String> {
}
