package com.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("record")
public class Record {
    @TableId(type = IdType.AUTO)
    private Long id;
    private BigDecimal amount;
    private String type;
    private Long categoryId;
    private Long userId;
    private LocalDate date;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}