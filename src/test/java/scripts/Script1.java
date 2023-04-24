package scripts;

import org.testng.annotations.Test;

import generic.BaseTest;
import generic.Utils;

public class Script1 extends BaseTest{
	@Test
	public void testA() {
		
		driver.getTitle();
		String un = Utils.readXL("./data/input.xlsx", "LoginPage", 1, 0);
		System.out.println("username"+un);
	}

}
