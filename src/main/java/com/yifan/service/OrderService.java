package com.yifan.service;

import com.yifan.entity.Order;

import java.util.Date;

/**
 * <p>Title: </p> 
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月01日 15:32
 */
public interface OrderService {

    Order findOne(Long id);

    Order create(Date when);

    boolean pay(Long id);

    boolean deliver(Long id);

    boolean received(Long id);

    boolean cancel(Long id);

}
