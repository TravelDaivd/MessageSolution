package com.david.message.solution.item.module;

import java.io.Serializable;

/**
 * @author gulei
 */
public class DeviceInfo implements Serializable {
    private String name;
    private String status;
    private int degrees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }
}
