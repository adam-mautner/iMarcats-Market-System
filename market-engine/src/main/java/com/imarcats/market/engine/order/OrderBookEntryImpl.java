package com.imarcats.market.engine.order;

import java.util.Iterator;

import com.imarcats.internal.server.interfaces.order.OrderBookEntryInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookEntrySize;
import com.imarcats.market.engine.order.OrderBookImpl.MatchTester;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.Order;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;


/**
 * Implementation of Order Book Entry functionality
 * @author Adam
 */
public class OrderBookEntryImpl implements OrderBookEntryInternal {

	private final OrderBookEntryModel _orderBookEntryModel;
	private final OrderBookImpl _orderBook;
	
	public OrderBookEntryImpl(OrderBookEntryModel orderBookEntryModel_, OrderBookImpl orderBook_) {
		super();
		_orderBookEntryModel = orderBookEntryModel_;
		_orderBook = orderBook_;
	}

	@Override
	public Quote getLimitQuote() {
		return _orderBookEntryModel.getLimitQuote();
	}

	@Override
	public OrderSide getSide() {
		return _orderBookEntryModel.getOrderSide();
	}

	@Override
	public OrderBookEntrySize getSize(boolean forClient_) {
		return getSize(forClient_, _orderBookEntryModel, _orderBook);
	}
	
	/**
	 * Returns the Aggregate Size of the Book Entry
	 * @param forClient_ Size is Calculated for Clients, NOT for the System, 
	 * 					 Hidden Orders (DisplayOrder = False) will not be added to the Calculation
	 * @param orderBookEntry_ Order Book Entry 
	 * @param orderBook_ Order Book 
	 * @return Size of the Entry (without Hidden Orders for Client, but tells if it has hidden Orders)
	 */
	static OrderBookEntrySize getSize(boolean forClient_, OrderBookEntryModel orderBookEntry_, OrderBookImpl orderBook_) {
		int aggregatedSize = 0;
		boolean displayOrders = true;
		for (Iterator<Long> orderKeyIter = orderBookEntry_.getOrderKeyIterator(); orderKeyIter.hasNext();) {
			Order order = orderBook_.getOrderByKey(orderKeyIter.next()).getOrderModel();
			
			displayOrders &= order.getDisplayOrder();

			if(!order.getDisplayOrder() && forClient_) {
				continue;
			}
			
			int remainingSize = order.calculateRemainingSize();
			aggregatedSize += remainingSize;
		}
		
		return new OrderBookEntrySize(aggregatedSize, !displayOrders);
	}
	
	/**
	 * Checks, if order can be matched with an Book Entry
	 * @param matchTester_ Match Tester 
	 * @param orderBookEntry_ Order Book Entry 
	 * @param orderBook_ Order Book 
	 *
	 */
	static void canOrderBeMatched(MatchTester matchTester_, OrderBookEntryModel orderBookEntry_, OrderBookImpl orderBook_) {
		for (Iterator<Long> orderKeyIter = orderBookEntry_.getOrderKeyIterator(); orderKeyIter.hasNext();) {
			OrderInternal order = orderBook_.getOrderByKey(orderKeyIter.next());
			
			matchTester_.testOrder(order);
			
			if(matchTester_.getStopMatching()) {
				break;
			}
		}
	}

	@Override
	public OrderType getType() {
		return getType(_orderBookEntryModel);
	}

	static OrderType getType(OrderBookEntryModel orderBookEntry_) {
		return orderBookEntry_.getOrderType();
	}

}
