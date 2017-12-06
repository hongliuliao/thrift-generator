/**
 * 
 */
package com.sohu.thrift.generator.test;

import java.util.List;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 下午1:04:16
 */
public class User {
	
	private int id;
	private String name;
	private boolean sex;
	private Status status;
	private double locX;
	private double locY;
	
	private List<Long> ids;
	
	private Account account;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sex
	 */
	public boolean isSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	/**
	 * @return the ids
	 */
	public List<Long> getIds() {
		return ids;
	}
	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
	public double getLocX() {
		return locX;
	}
	public void setLocX(double locX) {
		this.locX = locX;
	}
	public double getLocY() {
		return locY;
	}
	public void setLocY(double locY) {
		this.locY = locY;
	}
	
}
