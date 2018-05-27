package com.imarcats.market.engine.order;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import com.imarcats.interfaces.server.v100.order.QuoteOrderPrecedenceRule;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookEntryFactory;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

public class OrderPrecedenceRuleTest extends OrderCompareTestCaseBase {

	public void testTimePrecedenceRule() throws Exception {
		Order before = new Order();
		before.setSubmissionDate(new Date());
		
		// We need at least 10 millis time delay for the Time Precedence to work
		Thread.sleep(10);
		
		Order after = new Order();
		after.setSubmissionDate(new Date());
		
		Order sameTime1 = new Order();
		Order sameTime2 = new Order();
		Date now = new Date();
		sameTime1.setSubmissionDate(now);
		sameTime2.setSubmissionDate(now);

		assertEquals(0, TimeOrderPrecedenceRule.INSTANCE.compare(sameTime1, sameTime1));
		assertEquals(0, TimeOrderPrecedenceRule.INSTANCE.compare(sameTime1, sameTime2));
		assertEquals(0, TimeOrderPrecedenceRule.INSTANCE.compare(sameTime2, sameTime1));
		
		assertEquals(-1, TimeOrderPrecedenceRule.INSTANCE.compare(before, after));
		assertEquals(+1, TimeOrderPrecedenceRule.INSTANCE.compare(after, before));
	}
	
	public void testMarketOrderQuotePrecedence() throws Exception {
		Order marketOrder1 = new Order();
		marketOrder1.setType(OrderType.Market);
		
		Order marketOrder2 = new Order();
		marketOrder2.setType(OrderType.Market);		
		
		Order limitOrder = new Order();
		limitOrder.setType(OrderType.Limit);			
		limitOrder.setLimitQuoteValue(wrapQuote(100));
		
		QuoteOrderPrecedenceRule rule = new QuoteOrderPrecedenceRule(OrderSide.Buy, 10, QuoteType.Price);

		assertEquals(0, rule.compare(wrapOrderBookEntry(marketOrder1), wrapOrderBookEntry(marketOrder1)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(marketOrder1), wrapOrderBookEntry(marketOrder2)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(marketOrder2), wrapOrderBookEntry(marketOrder1)));
		
		assertEquals(-1, rule.compare(wrapOrderBookEntry(marketOrder1), wrapOrderBookEntry(limitOrder)));
		assertEquals(+1, rule.compare(wrapOrderBookEntry(limitOrder), wrapOrderBookEntry(marketOrder1)));
	}
	
	public void testSellSideOrderPricePrecedence() throws Exception {
		Order limitOrder1 = new Order();
		limitOrder1.setType(OrderType.Limit);			
		limitOrder1.setLimitQuoteValue(wrapQuote(100));

		Order limitOrder1a = new Order();
		limitOrder1a.setType(OrderType.Limit);			
		limitOrder1a.setLimitQuoteValue(wrapQuote(100));
		
		Order limitOrder2 = new Order();
		limitOrder2.setType(OrderType.Limit);			
		limitOrder2.setLimitQuoteValue(wrapQuote(110));
		
		QuoteOrderPrecedenceRule rule = new QuoteOrderPrecedenceRule(OrderSide.Sell, 10, QuoteType.Price);
		
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1a)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1a), wrapOrderBookEntry(limitOrder1)));
		
		assertEquals(-1, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder2)));
		assertEquals(+1, rule.compare(wrapOrderBookEntry(limitOrder2), wrapOrderBookEntry(limitOrder1)));
	}
	
	public void testSellSideOrderYieldPrecedence() throws Exception {
		Order limitOrder1 = new Order();
		limitOrder1.setType(OrderType.Limit);			
		limitOrder1.setLimitQuoteValue(wrapQuote(100));

		Order limitOrder1a = new Order();
		limitOrder1a.setType(OrderType.Limit);			
		limitOrder1a.setLimitQuoteValue(wrapQuote(100));
		
		Order limitOrder2 = new Order();
		limitOrder2.setType(OrderType.Limit);			
		limitOrder2.setLimitQuoteValue(wrapQuote(110));
		
		QuoteOrderPrecedenceRule rule = new QuoteOrderPrecedenceRule(OrderSide.Sell, 10, QuoteType.Yield);
		
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1a)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1a), wrapOrderBookEntry(limitOrder1)));
		
		assertEquals(-1, rule.compare(wrapOrderBookEntry(limitOrder2), wrapOrderBookEntry(limitOrder1)));
		assertEquals(+1, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder2)));
	}
	
	public void testBuySideOrderPricePrecedence() throws Exception {
		Order limitOrder1 = new Order();
		limitOrder1.setType(OrderType.Limit);			
		limitOrder1.setLimitQuoteValue(wrapQuote(100));

		Order limitOrder1a = new Order();
		limitOrder1a.setType(OrderType.Limit);			
		limitOrder1a.setLimitQuoteValue(wrapQuote(100));
		
		Order limitOrder2 = new Order();
		limitOrder2.setType(OrderType.Limit);			
		limitOrder2.setLimitQuoteValue(wrapQuote(110));
		
		QuoteOrderPrecedenceRule rule = new QuoteOrderPrecedenceRule(OrderSide.Buy, 10, QuoteType.Price);
		
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1a)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1a), wrapOrderBookEntry(limitOrder1)));
		
		assertEquals(-1, rule.compare(wrapOrderBookEntry(limitOrder2), wrapOrderBookEntry(limitOrder1)));
		assertEquals(+1, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder2)));
	}
	
	public void testBuySideOrderYieldPrecedence() throws Exception {
		Order limitOrder1 = new Order();
		limitOrder1.setType(OrderType.Limit);			
		limitOrder1.setLimitQuoteValue(wrapQuote(100));

		Order limitOrder1a = new Order();
		limitOrder1a.setType(OrderType.Limit);			
		limitOrder1a.setLimitQuoteValue(wrapQuote(100));
		
		Order limitOrder2 = new Order();
		limitOrder2.setType(OrderType.Limit);			
		limitOrder2.setLimitQuoteValue(wrapQuote(110));
		
		QuoteOrderPrecedenceRule rule = new QuoteOrderPrecedenceRule(OrderSide.Buy, 10, QuoteType.Yield);
		
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder1a)));
		assertEquals(0, rule.compare(wrapOrderBookEntry(limitOrder1a), wrapOrderBookEntry(limitOrder1)));
		
		assertEquals(-1, rule.compare(wrapOrderBookEntry(limitOrder1), wrapOrderBookEntry(limitOrder2)));
		assertEquals(+1, rule.compare(wrapOrderBookEntry(limitOrder2), wrapOrderBookEntry(limitOrder1)));
	}
	
	public void testUnrestrictedOrderPrecedence() throws Exception {
		Order before = new Order();
		Order after = new Order();
		after.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(-1, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(before, after));
		assertEquals(+1, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(after, before));
		
		after.setExecuteEntireOrderAtOnce(false);
		after.setMinimumSizeOfExecution(10);
		assertEquals(-1, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(before, after));
		assertEquals(+1, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(after, before));
		
		Order same = new Order();
		assertEquals(0, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(same, same));
		same.setExecuteEntireOrderAtOnce(true);
		assertEquals(0, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(same, same));
		same.setExecuteEntireOrderAtOnce(false);
		same.setMinimumSizeOfExecution(10);
		assertEquals(0, UnrestrictedOrderPrecedenceRule.INSTANCE.compare(same, same));
	}
	
	public void testDisplayPrecedence() throws Exception {
		Order before = new Order();
		before.setDisplayOrder(true);
		Order after = new Order();
		after.setDisplayOrder(false);
		
		assertEquals(-1, DisplayPrecedenceRule.INSTANCE.compare(before, after));
		assertEquals(+1, DisplayPrecedenceRule.INSTANCE.compare(after, before));
		
		Order same = new Order();
		assertEquals(0, DisplayPrecedenceRule.INSTANCE.compare(same, same));
		same.setDisplayOrder(false);
		assertEquals(0, DisplayPrecedenceRule.INSTANCE.compare(same, same));
	}

	public void testCompositePrecedence() throws Exception {
		SecondaryOrderPrecedenceRule[] rules = { 
				TimeOrderPrecedenceRule.INSTANCE, 
				UnrestrictedOrderPrecedenceRule.INSTANCE, 
				DisplayPrecedenceRule.INSTANCE }; 
		SecondaryOrderPrecedenceRule composite = new CompositePrecedenceRule(rules);
		
		Order before = new Order();
		Date submissionDateBefore = new Date();
		before.setSubmissionDate(submissionDateBefore);
		
		// We need at least 10 millis time delay for the Time Precedence to work
		Thread.sleep(10);
		
		Order after = new Order();
		Date submissionDateAfter = new Date();
		after.setSubmissionDate(submissionDateAfter);
		
		assertEquals(-1, composite.compare(before, after));
		assertEquals(+1, composite.compare(after, before));
		
		after.setSubmissionDate(before.getSubmissionDate());		
		after.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(-1, composite.compare(before, after));
		assertEquals(+1, composite.compare(after, before));

		after.setExecuteEntireOrderAtOnce(before.getExecuteEntireOrderAtOnce());
		after.setDisplayOrder(false);
		
		assertEquals(-1, composite.compare(before, after));
		assertEquals(+1, composite.compare(after, before));
		
		after.setDisplayOrder(before.getDisplayOrder());

		assertEquals(0, composite.compare(before, after));
		assertEquals(0, composite.compare(after, before));
		
		// check unrestricted as first 
		rules = new SecondaryOrderPrecedenceRule[]{ 
				UnrestrictedOrderPrecedenceRule.INSTANCE, 
				TimeOrderPrecedenceRule.INSTANCE}; 
		composite = new CompositePrecedenceRule(rules);
		
		before = new Order();
		submissionDateBefore = new Date();
		before.setSubmissionDate(submissionDateBefore);
		
		// We need at least 10 millis time delay for the Time Precedence to work
		Thread.sleep(10);
		
		after = new Order();
		submissionDateAfter = new Date();
		after.setSubmissionDate(submissionDateAfter);
		
		assertEquals(-1, composite.compare(before, after));
		assertEquals(+1, composite.compare(after, before));
		
		before.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(+1, composite.compare(before, after));
		assertEquals(-1, composite.compare(after, before));
	}
	
	@SuppressWarnings("serial")
	private OrderBookEntryModel wrapOrderBookEntry(Order order_) {		
		return OrderBookEntryFactory.createEntry(order_, new OrderBookModel() {
			
			@Override
			public void setVersionNumber(Long version_) {
				//does nothing
			}
			
			@Override
			public Long getVersionNumber() {
				//does nothing
				return 0L;
			}
			
			@Override
			public int size() {
				//does nothing
				return 0;
			}
			
			@Override
			public void remove(OrderBookEntryModel entry_) {
				//does nothing
			}
			
			@Override
			public boolean isEmpty() {
				//does nothing
				return false;
			}
			
			@Override
			public OrderSide getSide() {
				//does nothing
				return null;
			}
			
			@Override
			public Iterator<OrderBookEntryModel> getEntryIterator() {
				//does nothing
				return null;
			}
			
			@Override
			public OrderBookEntryModel get(int index_) {
				//does nothing
				return null;
			}
			
			@Override
			public int binarySearch(OrderBookEntryModel newEntry_,
					Comparator<OrderBookEntryModel> comparator_) {
				//does nothing
				return 0;
			}
			
			@Override
			public void add(int index_, OrderBookEntryModel entry_) {
				//does nothing
			}
			
			@Override
			public void add(OrderBookEntryModel entry_) {
				//does nothing
			}
		});
	}
	
}
