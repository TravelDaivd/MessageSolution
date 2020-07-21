package com.david.message.solution.item.module;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * <p>
 * 优惠券活动表
 * </p>
 *
 * @author gulei
 * @since 2020-02-14
 */
@Data

public class CouponActivity {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动开始时间
     */

    private String beginTime;

    /**
     * 活动结束时间
     */
    private String endTime;


    /**
     * 活动状态：0 未生成，1 已生成
     */
    private Integer isReceiver;

    /**
     * 活动状态：0 未开启，1 已开启， 2 已结束
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
