package com.imarcats.interfaces.server.v100.order;

import java.util.Date;

import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;

/**
 * Defines the set of Operations on the Order.
 * 
 * Note: Limit Order will NOT react to Corporate actions (like Dividends and Splits) and 
 *       it will NOT change its Limit Price - 
 *       Order is a Do Not Reduce (DNR) and Do Not Increase (DNI) Order 
 * TODO: Add this feature for Stock Markets 
 * @author Adam
 */
public interface OrderInterface {
	
	/**
	 * @return Order Key 
	 */
	public Long getKey();
	
	/**
	 * @return Side of the Order (Value from OrderSide)
	 */
	public OrderSide getSide();
	
	/**
	 * @return Type of the Order (Value from OrderType)
	 */
	public OrderType getType();
	
	/**
	 * @return Price or Yield Value Defined on the Order - Only for Limit Orders
	 */
	public Quote getLimitQuoteValue();
	
	/**
	 * @return Original Size of the Order
	 */
	public int getSize();

	/**
	 * @return Minimum Size that has to be Executed from the Order or 
	 * 		   -1, there is no minimum size 
	 */
	public int getMinimumSizeOfExecution();
	
	/**
	 * @return if the Order has to be fully Executed or left un-touched
	 */
	public boolean getExecuteEntireOrderAtOnce();
	
	/**
	 * @return if the Order Information will be displayed to the Public 
	 */
	public boolean getDisplayOrder();
	
	/**
	 * @return Remaining un-executed Size of the Order
	 */
	public int getRemainingSize();
	
	/**
	 * @return Executed Size of the Order
	 */
	public int getExecutedSize();
	
	/**
	 * @return State of the Order. Defines whether the Order is Submitted, Canceled. - 
	 * (Value from OrderState)
	 */
	public OrderState getState();
	
	/**
	 * @return Trigger Instruction for the Order - (Value from OrderTriggerInstruction)
	 */
	public OrderTriggerInstruction getTriggerInstruction();
	
	/**
	 * @return The copy of Trigger Instruction Properties for the Order
	 */
	public Property[] getTriggerProperties();
	
	/**
	 * @return Expiration Instruction for the Order - (Value from OrderExpirationInstruction)
	 */
	public OrderExpirationInstruction getExpirationInstruction();
	
	/**
	 * @return The copy of Expiration Instruction Properties for the Order
	 */
	public Property[] getExpirationProperties();
	
	/**
	 * @return Date and Time when the Order was submitted
	 */
	public Date getSubmissionDate();
	
	/**
	 * @return the User, who has Submitted the Order
	 */
	public String getSubmitterID();
	
	/**
	 * @return Information to the user, why his/her Order was canceled. 
     * 		   It is a language key used to retrieve actual text from I18N System 
	 */
	public String getCancellationCommentLanguageKey();
	
	/**
	 * @return Audit Information, like who created the Order and when
	 */
	public AuditInformation getCreationAudit();

}
