package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;


/**
 * Change Order Properties Request 
 * @author Adam
 *
 */
public class ChangeOrderPropertiesRequest extends OrderKeyRequestBase {

	private static final long serialVersionUID = 1L;
	private PropertyChangeDto[] _propertyChanges;

	public PropertyChangeDto[] getPropertyChanges() {
		return _propertyChanges;
	}
	public void setPropertyChanges(PropertyChangeDto[] propertyChanges_) {
		_propertyChanges = propertyChanges_;
	}
}
