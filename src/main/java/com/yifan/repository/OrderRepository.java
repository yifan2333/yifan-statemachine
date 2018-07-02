package com.yifan.repository;

import com.yifan.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月01日 15:14
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
