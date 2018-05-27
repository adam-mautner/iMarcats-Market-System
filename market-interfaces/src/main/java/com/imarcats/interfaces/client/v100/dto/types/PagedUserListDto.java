package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.interfaces.client.v100.dto.UserDto;

/**
 * Paged list of Users 
 * @author Adam
 */
public class PagedUserListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private UserDto[] _users;
	private String _cursorString;
	private int _maxNumberOfUsersOnPage;
	
	public UserDto[] getUsers() {
		return _users;
	}
	public void setUsers(UserDto[] users_) {
		_users = users_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfUsersOnPage() {
		return _maxNumberOfUsersOnPage;
	}
	public void setMaxNumberOfUsersOnPage(int maxNumberOfUsersOnPage_) {
		_maxNumberOfUsersOnPage = maxNumberOfUsersOnPage_;
	}
	@Override
	public String toString() {
		return "PagedUserList [_cursorString=" + _cursorString
				+ ", _maxNumberOfUsersOnPage=" + _maxNumberOfUsersOnPage
				+ ", _users=" + Arrays.toString(_users) + "]";
	}
	
}
