package com.github.cbuschka.selenium_glue;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumRule extends ExternalResource
{
	@Override
	protected void before() throws Throwable
	{
		new SeleniumTestContext(RemoteWebDriver.builder().build()).attach();
	}

	@Override
	protected void after()
	{
		SeleniumTestContext.current().detach();
	}
}
