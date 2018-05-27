package com.imarcats.internal.server.infrastructure.notification;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.DatastoreKey;

/**
 * Common Base Class for all Persisted Listeners
 * @author Adam
 */
@Entity
@Table(name="PERSISTED_LISTENER")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class PersistedListener {

	/**
	 * Primary Key of the Persisted Listener
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="LISTNER_KEY")
    private Long _listenerKey;

	/**
     * Key of the Observed Object
     */
	// choice 	
    @Column(name="OBSERVED_CODE_KEY", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
    private String _observedCodeKey;
    @Column(name="OBSERVED_ID_KEY")
    private Long _observedIdKey;
	// end of choice
	
	/**
	 * Class Name of the Observed Object (Order.class.getSimpleName())
	 */
    @Column(name="OBSERVED_CLASS_NAME", length=100)
	private String _observedClassName;

	/**
	 * Notification Type for this listener (like MarketDataChange or PropertyChanges)
	 */
    @Column(name="NOTIFICATION_TYPE")
    private NotificationType _notificationType;
    
	/**
	 * Persisted Listener framework will use this value to 
	 * selectively call some of the Listeners
	 */
    @Column(name="FILTER_STRING", length=50)
	private String _filterString = "";
	
	/**
	 * Creation Timestamp for the Listener 
	 */   
    @Column(name="CREATION_TIMESTAMP", length=50)
	private Timestamp _creationTimestamp;
    
	/**
	 * @return the Key of the Observed Object 
	 */
	public final DatastoreKey getObservedObjectKey() {
		DatastoreKey key = null;
		if(_observedCodeKey != null) {
			key = new DatastoreKey(_observedCodeKey);
		} else if(_observedIdKey != null) {
			key = new DatastoreKey(_observedIdKey);
		} else {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.LISTENER_CANNOT_BE_CREATED_WITH_NULL_OBSERVED_OBJECT, null, 
					new Object[] { this });
		} 
		
		return key;
	}
	
	/**
	 * Set the Observed 
	 * 
	 * Note: Persisted Listener framework will set this value
	 * @param key_ Key of the Observed Object
	 */
	public final void setObservedObjectKey(DatastoreKey key_) {
		_observedCodeKey = key_.getCodeKey();
		_observedIdKey = key_.getIdKey();
	}
	
	/**
	 * @return Primary Key of this Persisted Listener 
	 */
    public final Long getListenerKey() {
		return _listenerKey;
	}

	public final void setFilterString(String filterString) {
		_filterString = filterString;
	}

	public final String getFilterString() {
		return _filterString;
	}

	public void setObservedClassName(String observedClassName) {
		_observedClassName = observedClassName;
	}

	public String getObservedClassName() {
		return _observedClassName;
	}

	public void setNotificationType(NotificationType notificationType) {
		_notificationType = notificationType;
	}

	public NotificationType getNotificationType() {
		return _notificationType;
	}
	
	public void setCreationTimestamp(Timestamp creationTimestamp_) {
		_creationTimestamp = creationTimestamp_;
	}

	public Timestamp getCreationTimestamp() {
		return _creationTimestamp;
	}
	
	/**
	 * Persisted Listener framework will call this method, if the Listener is fired
	 * @param listenerParameters_ Parameters for this Listener Call
	 * @param listenerContext_ Context for this Listener Call
	 */
	public abstract void fireListener(ListenerCallParameters listenerParameters_, ListenerCallContext listenerContext_);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_creationTimestamp == null) ? 0 : _creationTimestamp
						.hashCode());
		result = prime * result
				+ ((_filterString == null) ? 0 : _filterString.hashCode());
		result = prime * result
				+ ((_listenerKey == null) ? 0 : _listenerKey.hashCode());
		result = prime
				* result
				+ ((_notificationType == null) ? 0 : _notificationType
						.hashCode());
		result = prime
				* result
				+ ((_observedClassName == null) ? 0 : _observedClassName
						.hashCode());
		result = prime
				* result
				+ ((_observedCodeKey == null) ? 0 : _observedCodeKey.hashCode());
		result = prime * result
				+ ((_observedIdKey == null) ? 0 : _observedIdKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistedListener other = (PersistedListener) obj;
		if (_creationTimestamp == null) {
			if (other._creationTimestamp != null)
				return false;
		} else if (!_creationTimestamp.equals(other._creationTimestamp))
			return false;
		if (_filterString == null) {
			if (other._filterString != null)
				return false;
		} else if (!_filterString.equals(other._filterString))
			return false;
		if (_listenerKey == null) {
			if (other._listenerKey != null)
				return false;
		} else if (!_listenerKey.equals(other._listenerKey))
			return false;
		if (_notificationType != other._notificationType)
			return false;
		if (_observedClassName == null) {
			if (other._observedClassName != null)
				return false;
		} else if (!_observedClassName.equals(other._observedClassName))
			return false;
		if (_observedCodeKey == null) {
			if (other._observedCodeKey != null)
				return false;
		} else if (!_observedCodeKey.equals(other._observedCodeKey))
			return false;
		if (_observedIdKey == null) {
			if (other._observedIdKey != null)
				return false;
		} else if (!_observedIdKey.equals(other._observedIdKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PersistedListener [_listenerKey=" + _listenerKey
				+ ", _observedCodeKey=" + _observedCodeKey
				+ ", _observedIdKey=" + _observedIdKey
				+ ", _observedClassName=" + _observedClassName
				+ ", _notificationType=" + _notificationType
				+ ", _filterString=" + _filterString + ", _creationTimestamp="
				+ _creationTimestamp + "]";
	}
}
