package com.david.message.solution.common;

public enum MessageStatus {

    WAITSEND(101,"待发送"),
    SENDSUCCESS(100,"发送成功"),
    SENDFAIL(102,"发送失败"),
    RETRYSENDFAIL(103,"重试发送失败"),
    RETRYCONSUMEFAIL(201,"重试消费失败"),
    CONSUMESUCCESS(200,"消费成功");


    private int code;
    private String status;

    MessageStatus(int code,String status){
        this.code=code;
        this.status=status;
   }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 自己定义一个静态方法,通过code返回枚举常量对象
     * @param code
     * @return
     */
    public static MessageStatus getValue(int code){
        for (MessageStatus messageStatus : values()) {
            if(messageStatus.getCode()== code){
                return messageStatus;
            }
        }
        return null;
    }

    /**
     * 自己定义一个静态方法,通过code返回枚举常量对象
     * @param status
     * @return
     */
    public static MessageStatus getCode(String status){
        for (MessageStatus messageStatus : values()) {
            if(messageStatus.getStatus().equals(status)){
                return messageStatus;
            }
        }
        return null;
    }

}
