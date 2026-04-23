package com.finance.controller;

import com.finance.entity.Category;
import com.finance.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        try {
            Category createdCategory = categoryService.create(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "创建分类失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable Long userId) {
        try {
            List<Category> categories = categoryService.findByUserId(userId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取分类失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<?> findByUserIdAndType(@PathVariable Long userId, @PathVariable String type) {
        try {
            List<Category> categories = categoryService.findByUserIdAndType(userId, type);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取分类失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Category category = categoryService.findById(id);
            if (category != null) {
                return ResponseEntity.ok(category);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "分类不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取分类失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Category category) {
        try {
            category.setId(id);
            Category updatedCategory = categoryService.update(category);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "更新分类失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "删除成功");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "删除分类失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}