package com.david.message.solution.item.reqparam;


import com.david.message.solution.item.module.Coupon;
import com.david.message.solution.item.module.CouponActivity;
import lombok.Data;

import java.util.List;

/**
 * @author gulei
 */
@Data
public class CouponActivityReq {

    private CouponActivity activity;
    private List<Coupon> coupons;


}
