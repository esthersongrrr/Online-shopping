package com.esther.itemservice.service;

import com.esther.itemservice.dao.ItemRepository;
import com.esther.itemservice.entity.Item;
import com.esther.itemservice.payload.Order2ItemDto;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ItemService {
    private ItemRepository itemRepository;

    private Gson gson;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public ItemService(ItemRepository itemRepository, Gson gson) {
        this.itemRepository = itemRepository;
        this.gson = gson;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(String id) {
        return itemRepository.findById(id);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }

    public Item changePartOfItem(String id, Map<String, Object> updates) {
        Item item = itemRepository.findById(id).orElseThrow();
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    item.setName((String) value);
                    break;
                case "price":
                    item.setPrice((Double) value);
                    break;
                case "description":
                    item.setDescription((String) value);
                    break;
                case "pictureUrls":
                    item.setPictureUrls((List<String>) value);
                    break;
                case "inventoryCount":
                    item.setInventoryCount((int) value);
            }
        });
        itemRepository.save(item);
        return item;
    }

    @KafkaListener(topics = "order-created")
    public void handleOrderCreated(String json) {
        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // Remove the leading and trailing quotes
            json = json.replace("\\\"", "\"");  // Unescape the internal quotes
        }
        System.out.println("Received JSON: " + json);
        Order2ItemDto orderDto = gson.fromJson(json, Order2ItemDto.class);
        Map<String, Integer> items = orderDto.getItems();
        Map<String, Integer> validItems = new HashMap<>();
        // for each item in items, update the amount (minus items.val) of item in itemRepository
        items.forEach((itemId, quantity) -> {
            Optional<Item> itemOpt = itemRepository.findById(itemId);
            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                if(item.getInventoryCount() < quantity) {
                    System.out.println("Item ID " + itemId + "is not enough cannot place order");
                    // Todo: strictly match the price when place the order
                    orderDto.setAmount(orderDto.getAmount().subtract(BigDecimal.valueOf(quantity*item.getPrice())));
                } else {
                    int newCount = item.getInventoryCount() - quantity;
                    item.setInventoryCount(newCount);
                    itemRepository.save(item);
                    validItems.put(itemId, quantity);
                    System.out.println("Updated inventory for item ID " + itemId + ": new count = " + newCount);
                }
            } else {
                System.out.println("Item ID " + itemId + " not found, cannot update inventory.");
            }
        });

        Order2ItemDto failedOrder2Item = new Order2ItemDto(orderDto.getId(), validItems, orderDto.getAmount());
        String orderJson = gson.toJson(failedOrder2Item);
        kafkaTemplate.send("item-placed", orderDto.getId().toString(), orderJson);
    }

    @KafkaListener(topics = "order-failed")
    public void handleOrderCancelled(String json) {
        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // Remove the leading and trailing quotes
            json = json.replace("\\\"", "\"");  // Unescape the internal quotes
        }
        System.out.println("Received JSON: " + json);
        Order2ItemDto orderDto = gson.fromJson(json, Order2ItemDto.class);
        Map<String, Integer> items = orderDto.getItems();
        items.forEach((itemId, quantity) -> {
            Optional<Item> itemOpt = itemRepository.findById(itemId);
            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                int newCount = item.getInventoryCount() + quantity;
                item.setInventoryCount(newCount);
                itemRepository.save(item);
                System.out.println("Updated inventory for item ID " + itemId + ": new count = " + newCount);
            } else {
                System.out.println("Item ID " + itemId + " not found, cannot update inventory.");
            }
        });
    }
}
