package com.david.message.solution.item.web;

import com.david.message.solution.item.module.Coupon;
import com.david.message.solution.item.reqparam.CouponActivityReq;
import com.david.message.solution.item.reqparam.ReceiverUserReq;
import com.david.message.solution.item.service.CouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author gulei
 */
@RestController
@RequestMapping("/activity")
public class CouponActivityController {

    @Autowired
    private CouponActivityService couponActivityService;

    @PostMapping("/add")
    public String add(@RequestBody CouponActivityReq couponActivityReq){
        if(couponActivityReq.getActivity() ==null){
            return "传入参数格式不正常";
        }
        Long activityId = couponActivityService.addActivityInfo(couponActivityReq);
        return "添加成功" +activityId;
    }


    @GetMapping("/coupon/{activityId}")
    public List<Coupon> getCouponByActivityId (@PathVariable("activityId")Long activityId){
        List<Coupon> coupons = couponActivityService.getCoupons(activityId);
        return coupons;
    }

    @PostMapping("/coupon/receiver")
    public String receiver(@RequestBody ReceiverUserReq receiverUserReq){

        couponActivityService.receiveCoupon(receiverUserReq);
        return "领取成功";
    }

    @GetMapping("/coupon/status/{couponReceiverId}")
    public String couponStatus (@PathVariable("couponReceiverId")Long couponReceiverId){
       couponActivityService.deleteCoupon(couponReceiverId);
        return "废除成功";
    }



}
