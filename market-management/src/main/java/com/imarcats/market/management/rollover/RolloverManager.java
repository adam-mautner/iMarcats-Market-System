package com.imarcats.market.management.rollover;

import java.util.Arrays;
import java.util.Date;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.Rollable;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Manages Roll-over of Products and Instruments 
 * @author Adam
 */
public class RolloverManager {

	private RolloverManager() { /* static utility class */ }
	
	/**
	 * Checks the Rollover Criteria for rolling over Source to Target
	 * @param source_ Source of the Rollover
	 * @param target_ Target of the Rollover
	 * @param targetUnderlying_ Underlying of the Target 
	 * @param rolloverExecutorUserID_ User Executing the Rollover
	 */
	public static void rollover(Rollable source_, Rollable target_, Rollable targetUnderlying_, String rolloverExecutorUserID_) {
		checkRolloverCriteria(source_, target_, targetUnderlying_);
		executeRollover(source_, target_, rolloverExecutorUserID_);
	}

	private static void executeRollover(Rollable source_, Rollable target_, String rolloverExecutorUserID_) {
		target_.setActivationDate(new Date());
		target_.setActivationStatus(ActivationStatus.Approved); 
		target_.setCreationAudit(createAudit(rolloverExecutorUserID_));
		target_.setRolloverAudit(createAudit(rolloverExecutorUserID_));
		target_.setCodeRolledFrom(source_.getCode());
		target_.setNewCode(DataUtils.adjustCodeToStandard(target_.getCode()));
	}
	
	private static AuditInformation createAudit(String rolloverExecutorUserID_) {
		AuditInformation auditInformation = new AuditInformation();
		auditInformation.setDateTime(new Date());
		auditInformation.setUserID(rolloverExecutorUserID_);
		
		return auditInformation;
	}

	private static void checkRolloverCriteria(Rollable source_,
			Rollable target_, Rollable targetUnderlying_) {
		// check rollable criteria 
		if(!source_.getRollable()) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_ROLLABLE_OBJECT_CANNOT_BE_ROLLED_OVER, null, new Object[] { source_ });
		}
		if(!target_.getRollable()) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.OBJECT_HAS_TO_REMAIN_ROLLABLE, null, new Object[] { target_ });	
		}	
		
		// check, if code of the source and target are different
		if(source_.getCode().equals(target_.getCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLED_OBJECTS_HAVE_SAME_CODE, null, new Object[] { source_, target_ });				
		}
		
		// check activation status
		checkActivationStatus(source_);
		
		// check underlying object
		if(source_.getUnderlyingCode() != null) {
			// check, if underlying deleted
			if(target_.getUnderlyingCode() == null) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNDERLYING_OBJECT_CANNOT_BE_REMOVED_WHILE_ROLLOVER, null, new Object[] { source_, target_ });					
			}
			
			// check target underlying provided
			if(targetUnderlying_ == null ||
			   !target_.getUnderlyingCode().equals(targetUnderlying_.getCode())) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INVALID_UNDELYING_OBJECT_PROVIDED_FOR_ROLLOVER, null, new Object[] { target_, targetUnderlying_ });					
			} 
			
			// check, if underlying is the same 
			if(!source_.getUnderlyingCode().equals(target_.getUnderlyingCode())) {				
				// check, if underlying was rolled from the right object
				if(!source_.getUnderlyingCode().equals(targetUnderlying_.getCodeRolledFrom())) {
					throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNDERLYING_OBJECT_PROVIDED_FOR_ROLLOVER_IS_NOT_ROLLED_FROM_UNDERLYING_OF_SOURCE_OBJECT, null, new Object[] { source_, target_, targetUnderlying_ });					
				}
			}

			// check activation status
			checkActivationStatus(targetUnderlying_);
		} else {
			// check, if underlying added
			if(target_.getUnderlyingCode() != null) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNDERLYING_OBJECT_CANNOT_BE_ADDED_WHILE_ROLLOVER, null, new Object[] { source_, target_ });					
			}
		}
		
		// check rollable properties 
		checkRollablePropertyNames(source_, target_);
		
		// check properties, if they are changed
		checkProperties(source_, target_);
	}
	
	private static void checkProperties(Rollable source_, Rollable target_) {
		String[] sourceRollableProperties = getRollableProperties(source_);
		
		Property[] sourceProperties = getProperties(source_);
		Property[] targetProperties = getProperties(target_);
		
		if(sourceProperties.length != targetProperties.length) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLOVER_CANNOT_CHANGE_NUMBER_OF_PROPERTIES, null, new Object[] { sourceProperties, targetProperties });
		}
		
		for (Property targetProperty : targetProperties) {
			Property sourceProperty = PropertyUtils.findProperty(targetProperty.getName(), sourceProperties);
			if(sourceProperty == null) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLOVER_CANNOT_ADD_OR_REMOVE_PROPERTIES, null, new Object[] { targetProperty.getName(), sourceProperties });
			} 
			
			if(!sourceProperty.equalsProperty(targetProperty)) {
				if(!checkNameOnList(sourceProperty.getName(), sourceRollableProperties)) {
					throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLOVER_CANNOT_CHANGE_NON_ROLLABLE_PROPERTIES, null, new Object[] { sourceProperty, targetProperty });
				}
			}
		}
	}
	
	private static void checkRollablePropertyNames(Rollable source_, Rollable target_) {
		String[] sourceRollableProperties = getRollableProperties(source_);
		String[] targetRollableProperties = getRollableProperties(target_);		
		
		MarketRuntimeException exception = MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLOVER_CANNOT_CHANGE_LIST_OF_ROLLABLE_PROPERTIES, null, new Object[] { sourceRollableProperties, targetRollableProperties });
		
		if(sourceRollableProperties.length != targetRollableProperties.length) {
			throw exception;
		} 
		
		for (String sourceName : sourceRollableProperties) {
			if(!checkNameOnList(sourceName, targetRollableProperties)) {
				throw exception;
			}
		}
	}
	
	private static Property[] getProperties(Rollable rollable_) {
		return rollable_.getProperties() != null 
				? rollable_.getProperties() 
				: new Property[0];
	} 
	
	private static boolean checkNameOnList(String name_, String[] list_) {
		boolean found = Arrays.asList(list_).indexOf(name_) >= 0;
		
		return found;
	}
	
	private static String[] getRollableProperties(Rollable rollable_) {
		return rollable_.getRollablePropertyNames() != null 
					? rollable_.getRollablePropertyNames().toArray(new String[rollable_.getRollablePropertyNames().size()]) 
					: new String[0]; 
	}
	
	public static void checkActivationStatus(Rollable rollable_) {
		if(rollable_.getActivationStatus() != ActivationStatus.Activated &&
		   rollable_.getActivationStatus() != ActivationStatus.Approved) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, null, new Object[] { rollable_ });					
		}
	}
}
