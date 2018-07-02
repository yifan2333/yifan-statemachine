package com.yifan.entity;

import com.yifan.enums.OrderStates;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月01日 15:14
 */
@Data
@Entity(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String state;

    private Date date;

    public Order() {
    }

    public Order(Date when) {
        this.date = when;
        setStateByEnum(OrderStates.WAIT_PAYMENT);
    }

    public OrderStates getStatusEnum() {
        return OrderStates.valueOf(state);
    }

    private void setStateByEnum(OrderStates os) {
        this.state = os.name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", date=" + date +
                '}';
    }
}
