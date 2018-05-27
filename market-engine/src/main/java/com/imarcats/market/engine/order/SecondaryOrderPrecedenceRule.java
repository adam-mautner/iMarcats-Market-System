package com.imarcats.market.engine.order;

import java.util.Comparator;

import com.imarcats.model.Order;

/**
 * Order Precedence Rule of the Market
 * @author Adam
 */
public interface SecondaryOrderPrecedenceRule extends Comparator<Order> {

}
