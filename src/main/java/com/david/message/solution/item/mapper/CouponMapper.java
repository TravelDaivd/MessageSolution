package com.david.message.solution.item.mapper;

import com.david.message.solution.item.module.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author gulei
 */
@Mapper
@Repository
public interface CouponMapper extends CommonMapper<Coupon> {

}
