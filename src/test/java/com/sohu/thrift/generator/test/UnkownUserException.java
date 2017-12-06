/**
 * 
 */
package com.sohu.thrift.generator.test;

/**
 * @author liao
 *
 */
public class UnkownUserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	

}
