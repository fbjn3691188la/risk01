package com.finance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finance.entity.Record;
import com.finance.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class StatisticsService {
    @Autowired
    private RecordMapper recordMapper;

    public Map<String, Object> getMonthlyStatistics(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .ge("date", startDate)
               .le("date", endDate);

        List<Record> records = recordMapper.selectList(wrapper);

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryIncome = new HashMap<>();
        Map<String, BigDecimal> categoryExpense = new HashMap<>();

        for (Record record : records) {
            if ("income".equals(record.getType())) {
                income = income.add(record.getAmount());
                // 这里简化处理，实际应该关联分类表获取分类名称
                String categoryKey = "category_" + record.getCategoryId();
                categoryIncome.put(categoryKey, categoryIncome.getOrDefault(categoryKey, BigDecimal.ZERO).add(record.getAmount()));
            } else if ("expense".equals(record.getType())) {
                expense = expense.add(record.getAmount());
                String categoryKey = "category_" + record.getCategoryId();
                categoryExpense.put(categoryKey, categoryExpense.getOrDefault(categoryKey, BigDecimal.ZERO).add(record.getAmount()));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("income", income);
        result.put("expense", expense);
        result.put("balance", income.subtract(expense));
        result.put("categoryIncome", categoryIncome);
        result.put("categoryExpense", categoryExpense);

        return result;
    }

    public Map<String, Object> getYearlyStatistics(Long userId, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .ge("date", startDate)
               .le("date", endDate);

        List<Record> records = recordMapper.selectList(wrapper);

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        Map<Integer, BigDecimal> monthlyIncome = new HashMap<>();
        Map<Integer, BigDecimal> monthlyExpense = new HashMap<>();

        // 初始化月度数据
        for (int i = 1; i <= 12; i++) {
            monthlyIncome.put(i, BigDecimal.ZERO);
            monthlyExpense.put(i, BigDecimal.ZERO);
        }

        for (Record record : records) {
            int month = record.getDate().getMonthValue();
            if ("income".equals(record.getType())) {
                income = income.add(record.getAmount());
                monthlyIncome.put(month, monthlyIncome.get(month).add(record.getAmount()));
            } else if ("expense".equals(record.getType())) {
                expense = expense.add(record.getAmount());
                monthlyExpense.put(month, monthlyExpense.get(month).add(record.getAmount()));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("income", income);
        result.put("expense", expense);
        result.put("balance", income.subtract(expense));
        result.put("monthlyIncome", monthlyIncome);
        result.put("monthlyExpense", monthlyExpense);

        return result;
    }
}