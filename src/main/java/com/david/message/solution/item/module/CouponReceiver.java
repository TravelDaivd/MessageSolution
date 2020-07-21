package com.david.message.solution.item.module;


import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * <p>
 * 优惠券获得者表
 * </p>
 * @since 2020-02-12
 */

@Data
public class CouponReceiver  {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 领取者ID
     */
    private Long userId;

    /**
     * 用户手机号码
     */
    private String userPhone;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 状态：未领用、已领用、使用中、已使用、已过期，已作废
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdStime;

    /**
     * 更新时间
     */
    private LocalDateTime modifiedStime;


}
