package com.david.message.solution.item.tasktime;


import com.david.message.solution.item.mapper.CouponActivityMapper;
import com.david.message.solution.item.mapper.CouponMapper;
import com.david.message.solution.item.mapper.CouponReceiverMapper;
import com.david.message.solution.item.module.Coupon;
import com.david.message.solution.item.module.CouponActivity;
import com.david.message.solution.item.module.CouponReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gulei
 */
@Component
public class ReceiverTask {
    public final static long SECOND = 1 * 1000;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CouponActivityMapper couponActivityMapper;
    @Autowired
    private CouponReceiverMapper couponReceiverMapper;
    @Autowired
    private CouponMapper couponMapper;

    @Scheduled(fixedRate = SECOND * 60 * 1, initialDelay = SECOND * 5)
    public void receiverTaskTime(){
        CouponActivity activity = new CouponActivity();
        activity.setIsReceiver(0);
        List<CouponActivity> activityList = couponActivityMapper.select(activity);
        if(activityList.size() >0){
            activityList.parallelStream().forEach(couponActivity -> {
                handleCouponInfo(couponActivity);
            });
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void handleCouponInfo(CouponActivity activity){
        logger.info(String.format("生成优惠券空领用人的活动信息：活动名称【%s】，活动ID【%s】",activity.getTitle(),activity.getId()));
        Coupon coupon = new Coupon();
        coupon.setActivityId(activity.getId());
        List<Coupon> coupons = couponMapper.select(coupon);
        List<CouponReceiver> couponReceivers = new ArrayList<>();
        for(Coupon coupon1 : coupons){
            Long totalNumber = coupon1.getTotalNumber();
            for(long i =0;i<totalNumber;i++){
                CouponReceiver couponReceiver = new CouponReceiver();
                couponReceiver.setActivityId(coupon1.getActivityId());
                couponReceiver.setCouponId(coupon1.getId());
                couponReceiver.setModifiedStime(LocalDateTime.now());
                couponReceiver.setCreatedStime(LocalDateTime.now());
                couponReceiver.setStatus(1);
                couponReceivers.add(couponReceiver);
            }
        }

        couponReceiverMapper.insertList(couponReceivers);
        activity.setIsReceiver(1);
        activity.setModifiedStime(LocalDateTime.now());
        couponActivityMapper.updateByPrimaryKey(activity);

    }
}
