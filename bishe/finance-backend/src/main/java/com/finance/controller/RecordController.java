package com.finance.controller;

import com.finance.entity.Record;
import com.finance.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Record record) {
        try {
            Record createdRecord = recordService.create(record);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "创建记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable Long userId) {
        try {
            List<Record> records = recordService.findByUserId(userId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Record record = recordService.findById(id);
            if (record != null) {
                return ResponseEntity.ok(record);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "记录不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Record record) {
        try {
            record.setId(id);
            Record updatedRecord = recordService.update(record);
            return ResponseEntity.ok(updatedRecord);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "更新记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            recordService.delete(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "删除成功");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "删除记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<?> findByUserIdAndType(@PathVariable Long userId, @PathVariable String type) {
        try {
            List<Record> records = recordService.findByUserIdAndType(userId, type);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取记录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}