package com.finance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finance.entity.Record;
import com.finance.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;

    public Record create(Record record) {
        recordMapper.insert(record);
        return record;
    }

    public List<Record> findByUserId(Long userId) {
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return recordMapper.selectList(wrapper);
    }

    public Record findById(Long id) {
        return recordMapper.selectById(id);
    }

    public Record update(Record record) {
        recordMapper.updateById(record);
        return record;
    }

    public void delete(Long id) {
        recordMapper.deleteById(id);
    }

    public List<Record> findByUserIdAndType(Long userId, String type) {
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("type", type);
        return recordMapper.selectList(wrapper);
    }
}