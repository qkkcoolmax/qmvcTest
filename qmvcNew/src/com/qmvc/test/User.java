package com.qmvc.test;

public class User {

	public User() {
		// TODO Auto-generated constructor stub
	}

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	private String age;
	private double income;

	@Override
	public String toString() {
		return username+","+income+","+age;
	}
}
