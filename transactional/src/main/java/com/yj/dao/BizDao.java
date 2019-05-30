package com.yj.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BizDao {
	public int updateBookStock(long id);

	public int updateAccountBalance(long id);
}
