package com.finance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finance.entity.Category;
import com.finance.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public Category create(Category category) {
        categoryMapper.insert(category);
        return category;
    }

    public List<Category> findByUserId(Long userId) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return categoryMapper.selectList(wrapper);
    }

    public List<Category> findByUserIdAndType(Long userId, String type) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("type", type);
        return categoryMapper.selectList(wrapper);
    }

    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    public Category update(Category category) {
        categoryMapper.updateById(category);
        return category;
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}