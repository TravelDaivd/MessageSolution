package com.david.message.solution.item.service;

import com.david.message.solution.item.mapper.CouponMapper;
import com.david.message.solution.item.mapper.CouponReceiverMapper;
import com.david.message.solution.item.module.Coupon;
import com.david.message.solution.item.module.CouponActivity;
import com.david.message.solution.item.module.CouponReceiver;
import com.david.message.solution.item.reqparam.CouponActivityReq;
import com.david.message.solution.item.reqparam.ReceiverUserReq;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.david.message.solution.item.mapper.CouponActivityMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gulei
 */
@Service
public class CouponActivityService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CouponActivityMapper couponActivityMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponReceiverMapper couponReceiverMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Transactional(rollbackFor=Exception.class)
    public Long addActivityInfo (CouponActivityReq couponActivityReq){

        CouponActivity couponActivity = couponActivityReq.getActivity();
        List<Coupon> coupons = couponActivityReq.getCoupons();
        if(coupons.size() == 0){
            return 0L;
        }
        couponActivity.setIsReceiver(0);
        couponActivity.setModifiedStime(LocalDateTime.now());
        couponActivity.setCreatedStime(LocalDateTime.now());
        //添加活动
        couponActivityMapper.insert(couponActivity);


        // 添加要生成的优惠券
        coupons.parallelStream().forEach(coupon -> {
            coupon.setActivityId(couponActivity.getId());
            coupon.setModifiedStime(LocalDateTime.now());
            coupon.setCreatedStime(LocalDateTime.now());
        });

        couponMapper.insertList(coupons);
        return couponActivity.getId();
    }
    /**
     * 当前活动下生成的优惠劵
     */
    public List<Coupon> getCoupons(Long activityId){
        String key = "activity_"+activityId;
        ;
        try {

            String redisValue = redisTemplate.opsForValue().get(key);
            if(StringUtils.isEmpty(redisValue)){
                Coupon coupon = new Coupon();
                coupon.setActivityId(activityId);
                List<Coupon> coupons = couponMapper.select(coupon);
                Gson gson = new Gson();
                redisValue = gson.toJson(coupons);
                Boolean bool = redisTemplate.opsForValue().setIfAbsent(key, redisValue,1,TimeUnit.HOURS);
                if(bool){
                    logger.info(String.format("把当前活动下的优惠券保存到redis成功 活动ID:%s;优惠券信息：%s",activityId,redisValue));
                }else{
                    logger.error(String.format("把当前活动下的优惠券保存到redis失败 活动ID:%s",activityId));
                }
                return coupons;
            }else{
                Gson gson = new Gson();
                List<Coupon> couponsList =gson.fromJson(redisValue, new TypeToken<List<Coupon>>(){}.getType());
                logger.info(String.format("select coupons data form redis：%s", gson.toJson(redisValue)));
                return couponsList;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(String.format("查询活动下的优惠券信息出现了错误：%s",e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void  deleteCoupon(Long couponReceiverId){
        try{
            String redisReceiverId = redisTemplate.opsForValue().get("receive_del");
            if(redisReceiverId.equals(couponReceiverId)){
                logger.info(String.format("在高并发下废除当前优惠券时有用户在领用此优惠券：优惠券ID : %s",couponReceiverId));
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(String.format("废除优惠券前,检查redis锁出现错误： %s",e.getMessage()));
        }
        CouponReceiver couponReceiver = new CouponReceiver();
        couponReceiver.setId(couponReceiverId);
        couponReceiver.setStatus(6);
        couponReceiver.setModifiedStime(LocalDateTime.now());
        couponReceiverMapper.updateByPrimaryKey(couponReceiver);
    }


    /**
     * 用户能领取多少优惠券
     */
    public  void  receiveCoupon(ReceiverUserReq receiverUserReq){
        boolean bool = false;
        try{
            Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("receive_lock", receiverUserReq.getUserPhone(), 2, TimeUnit.SECONDS);
            if(!aBoolean){
                bool = true;
            }
        if(!bool){
            CouponReceiver couponReceiver = new CouponReceiver();
            couponReceiver.setCouponId(receiverUserReq.getCouponId());
            couponReceiver.setStatus(1);
            List<CouponReceiver> couponReceivers = couponReceiverMapper.select(couponReceiver);
            if(couponReceivers.size()== 0){
                logger.info(String.format("优惠券已经领取完：优惠券ID:%s",receiverUserReq.getCouponId()));
                return;
            }
            couponReceiver = couponReceivers.get(0);
            couponReceiver.setUserId(receiverUserReq.getUserId());
            couponReceiver.setUserPhone(receiverUserReq.getUserPhone());
            couponReceiver.setStatus(2);
            couponReceiver.setModifiedStime(LocalDateTime.now());
            couponReceiverMapper.updateByPrimaryKey(couponReceiver);
            updateCouponReceiveNumber(couponReceiver.getCouponId());
            logger.info(String.format("优惠券领用一张，优惠券Id:[%s]",couponReceiver.getId()));
            redisTemplate.delete("receive_lock");
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 更改领用优惠券数量
     */
    private void updateCouponReceiveNumber(Long couponId){
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        coupon = couponMapper.selectOne(coupon);
        Long receiveNumber = coupon.getReceiveNumber()+1;
        coupon.setReceiveNumber(receiveNumber);
        coupon.setModifiedStime(LocalDateTime.now());
        couponMapper.updateByPrimaryKey(coupon);
    }



}
