package com.site.datadriven;

import java.util.ArrayList;

public class YamlTestData {
	private String login;
	private String address;
	private ArrayList<AddressBook> addressBook;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ArrayList<AddressBook> getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(ArrayList<AddressBook> addressBook) {
		this.addressBook = addressBook;
	}
}
