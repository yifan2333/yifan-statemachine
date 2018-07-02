package com.yifan.entity;

import com.yifan.enums.OrderStates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

@Entity(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String state;

    private Date date;

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
}
