package com.imarcats.market.engine.service;

import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderBookModelDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMatchedTradeSideListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedOrderListDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.messages.response.CurrentPositionListResponse;
import com.imarcats.interfaces.client.v100.messages.response.CurrentPositionResponse;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.MatchedTradeDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.OrderDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.market.engine.order.OrderManagementSystem;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.PagedMatchedTradeSideList;
import com.imarcats.model.types.PagedOrderList;

/**
 * Implementation of the Market Service, which provides a Low-Level API to 
 * the Market Engine. 
 * 
 * @author Adam
 */
public class MarketService implements MarketServiceInternalFacade {

	private final MarketDatastore _marketDatastore;
	private final OrderManagementSystem _orderManagementSystem;

	public MarketService(MarketDatastore marketDatastore_, OrderManagementSystem orderManagementSystem_) {
		super();
		_marketDatastore = marketDatastore_;
		_orderManagementSystem = orderManagementSystem_;
	}

	// Market Related Functions 
	@Override
	public MarketDto findMarketByCode(String marketCode_) {
		MarketInternal marketInternal = getAndCheckMarket(marketCode_);
		
		Market marketModel = marketInternal.getMarketModel();
		
		// copy Best Bid and Ask for Clients
		// TODO: Put this on the DTO instead 
		marketModel.setCurrentBestBid(marketInternal.getBestBid());
		marketModel.setCurrentBestAsk(marketInternal.getBestAsk());
		
		return MarketDtoMapping.INSTANCE.toDto(marketModel);
	}

	private MarketInternal getAndCheckMarket(String marketCode_) {
		MarketInternal marketInternal = _marketDatastore.findMarketBy(marketCode_);
		
		// TODO: Check trade permissions and user access rights !!!
		
		if(marketInternal == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET, 
					null, new Object[] { "Market ID=" + marketCode_ });
		}
		return marketInternal;
	}
	
	@Override
	public OrderBookModelDto getOrderBookFor(String marketCode_, com.imarcats.interfaces.client.v100.dto.types.OrderSide orderSide_) { 
		OrderSide orderSide = OrderDtoMapping.INSTANCE.fromDto(orderSide_);
		
		// TODO: Check, if this needs to be in a Transaction 
		MarketInternal marketInternal = getAndCheckMarket(marketCode_);
		
		OrderBookInternal book = null;
		if(orderSide == OrderSide.Buy) {
			book = marketInternal.getBuyBook();
		} else if(orderSide == OrderSide.Sell) {
			book = marketInternal.getSellBook();
		} else {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_SIDE_CANNOT_BE_IDENTIFIED, 
					null, new Object[] { orderSide_, marketInternal });
		}
		
		return MarketDtoMapping.INSTANCE.toDto(book.getOrderBookForClient());
	}
	// End of Market Related Functions 
	
	// Order Related Functions
	@Override
	public Long createOrder(String marketCode_, OrderDto orderDto_, OrderManagementContext orderManagementContext_, String userID_) {
		return _orderManagementSystem.createOrder(marketCode_, OrderDtoMapping.INSTANCE.fromDto(orderDto_), userID_, orderManagementContext_);
	}
	
	@Override
	public void submitOrder(Long orderKey_, OrderManagementContext orderManagementContext_, String userID_) {
		_orderManagementSystem.submitOrder(orderKey_, userID_, orderManagementContext_);
	}
	
	@Override
	public void cancelOrder(Long orderKey_, OrderManagementContext orderManagementContext_, String userID_) {
		_orderManagementSystem.cancelOrder(orderKey_, userID_, orderManagementContext_);
	}

	@Override
	public void changeOrderProperties(Long orderKey_, PropertyChangeDto[] propertyChanges_, OrderManagementContext orderManagementContext_, String userID_) {
		_orderManagementSystem.changeOrderProperties(orderKey_, propertyChanges_, userID_, orderManagementContext_);
	}

	@Override
	public void deleteOrder(Long orderKey_, OrderManagementContext orderManagementContext_, String userID_) {
		_orderManagementSystem.deleteOrder(orderKey_, userID_, orderManagementContext_);
	}

	@Override
	public OrderDto getOrder(Long orderKey_) {
		return toDto(_orderManagementSystem.getOrder(orderKey_).getOrderModel());
	}
	
	@Override
	public PagedOrderListDto getOrdersForUserAndMarket(String marketCode_, String cursorString_, int maxNumberOfOrderOnPage_, String userID_) {
		return toDto(_orderManagementSystem.getOrdersFor(marketCode_, userID_, cursorString_, maxNumberOfOrderOnPage_));
	}

	@Override
	public PagedOrderListDto getOrdersForUser(String cursorString_, int maxNumberOfOrderOnPage_, String userID_) {
		return toDto(_orderManagementSystem.getOrdersFor(userID_, cursorString_, maxNumberOfOrderOnPage_));
	}

	public PagedOrderListDto toDto(PagedOrderList orderList_) {
		return OrderDtoMapping.INSTANCE.toDto(orderList_);
	}

	public PagedOrderList fromDto(PagedOrderListDto orderList_) {
		return OrderDtoMapping.INSTANCE.fromDto(orderList_);
	}
	
	public OrderDto toDto(Order order_) {
		return OrderDtoMapping.INSTANCE.toDto(order_);
	}

	public Order fromDto(OrderDto order_) {
		return OrderDtoMapping.INSTANCE.fromDto(order_);
	}
	// End of Order Related Functions

	// Trade Related Functions
	@Override
	public PagedMatchedTradeSideListDto getMatchedTradesForUserAndMarket(String marketCode_,
			String cursorString_, int maxNumberOfTradesOnPage_, 
			String userID_) {
		return toDto(_orderManagementSystem.getMatchedTradesFor(marketCode_,
				userID_, cursorString_,
				maxNumberOfTradesOnPage_));
	}

	@Override
	public PagedMatchedTradeSideListDto getMatchedTradesForUser(
			String cursorString_, int maxNumberOfTradesOnPage_, 
			String userID_) {
		return toDto(_orderManagementSystem.getMatchedTradesFor(
				userID_, cursorString_,
				maxNumberOfTradesOnPage_));
	}

	private PagedMatchedTradeSideListDto toDto(PagedMatchedTradeSideList tradeList_) {
		return MatchedTradeDtoMapping.INSTANCE.toDto(tradeList_);
	}
	// End of Trade Related Functions
	
	// Position Related Functions
	@Override
	public CurrentPositionResponse getPosistionForUserAndMarket(String marketCode_,String userID_) {
		return _orderManagementSystem.getPositionFor(marketCode_, userID_);
	}

	
	@Override
	public CurrentPositionListResponse getPosistionForUser(
			String userID_) {
		return _orderManagementSystem.getPositionFor(userID_); 
	}
	// End of Position Related Functions
}
