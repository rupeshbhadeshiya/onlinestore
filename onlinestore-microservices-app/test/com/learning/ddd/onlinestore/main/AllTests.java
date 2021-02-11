package com.learning.ddd.onlinestore.main;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

//@RunWith(Suite.class)
//@SuiteClasses({
//	InventoryTest.class,
//	ShoppingCartTest.class,
//	CheckoutServiceTest.class
//})


@RunWith(JUnitPlatform.class)
@SuiteDisplayName("All Tests for Online Store")
@SelectPackages("com.learning.ddd.onlinestore.main")
public class AllTests {

}
