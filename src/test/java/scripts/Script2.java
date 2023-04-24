package scripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import generic.BaseTest;

public class Script2 extends BaseTest{
	@Test
	public void testB() {
		
		Assert.fail("Error message");
	}
}
