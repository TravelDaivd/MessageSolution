package com.david.message.solution.item.reqparam;

import lombok.Data;

/**
 * @author gulei
 */
@Data
public class ReceiverUserReq {

    private Long userId;
    private String userPhone;
    private Long couponId;

}
