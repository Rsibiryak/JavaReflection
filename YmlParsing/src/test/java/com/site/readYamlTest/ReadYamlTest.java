package com.site.readYamlTest;

import java.util.ArrayList;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.site.basetest.AbstractTest;
import com.site.datadriven.AddressBook;
import com.site.datadriven.DataDriven;

/**
 * Check, test data from yaml fail were read and assigned to class fields
 * correct.
 * 
 * @author Alexander_Suvorov
 */
@DataDriven
public class ReadYamlTest extends AbstractTest {
	private String login;
	private String address;
	private ArrayList<AddressBook> addressBook;

	@Test
	public void checkClassFields() {
		SoftAssert softAsert = new SoftAssert();

		softAsert.assertEquals(login, "mytest2", "Values are different");
		softAsert.assertEquals(address, "4011 16th Ave S", "Values are different");

		softAsert.assertEquals(addressBook.size(), 2);
		softAsert.assertEquals(addressBook.get(0).getName(), "Home");
		softAsert.assertEquals(addressBook.get(1).getNumber(), "425-555-2306");

		softAsert.assertAll();
	}
}
