package com.esther.itemservice.controller;


import com.esther.itemservice.entity.Item;
import com.esther.itemservice.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable String id) {
        return itemService.getItemById(id).orElse(null);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @PutMapping("/{id}")
    public Item changeItem(@RequestBody Item item) {
        return itemService.updateItem(item);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return itemService.changePartOfItem(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
    }

    @PatchMapping("/{id}/inventory")
    public Item updateInventory(@PathVariable String id, @RequestParam int amount) {
        Item item = itemService.getItemById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setInventoryCount(item.getInventoryCount() + amount);
        return itemService.updateItem(item);
    }
}
