package com.david.message.solution.item.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Mapper接口：基本的增、删、改、查方法 - Mapper的作用：自动生成增删改查的SQL语句 大大减化对单表的操作
 * MySqlMapper：针对MySQL的额外补充接口，支持批量插入
 **/
public interface CommonMapper<T> extends Mapper<T>, MySqlMapper<T> {
	
}