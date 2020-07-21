package com.david.message.solution.item.module;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author gulei
 */
@Data
public class Coupon {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 优惠券名称
     */
    private String moduleName;

    /**
     * 类型：1 满减券，2 折扣券
     */
    private Integer type;

    /**
     * 满多少钱，才减，单位 为分
     */
    private Integer fullMoney;

    /**
     * 打几折，单位为1%，比如七五折为75
     */
    private Integer discountPercent;

    /**
     * 优惠券总数
     */
    private Long totalNumber;

    /**
     * 领取数量
     */
    private Long receiveNumber;

    /**
     * 使用数量
     */
    private Long useNumber;


    /**
     * 创建时间
     */
    private LocalDateTime createdStime;

    /**
     * 更新时间
     */
    private LocalDateTime modifiedStime;



}
