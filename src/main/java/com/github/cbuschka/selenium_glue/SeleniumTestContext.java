package com.github.cbuschka.selenium_glue;

import org.openqa.selenium.WebDriver;

public class SeleniumTestContext
{
	private static ThreadLocal<SeleniumTestContext> threadLocal = new ThreadLocal<>();

	private int attachCount = 0;

	private WebDriver webDriver;

	public static SeleniumTestContext current()
	{
		SeleniumTestContext curr = threadLocal.get();
		if (curr == null)
		{
			throw new IllegalStateException("No current SeleniumTestContext.");
		}

		return curr;
	}

	SeleniumTestContext(WebDriver webDriver)
	{
		this.webDriver = webDriver;
	}

	public void run(Runnable r)
	{
		try
		{
			attach();

			r.run();
		}
		finally
		{
			detach();
		}
	}

	public WebDriver getWebDriver()
	{
		checkCurrent();

		return webDriver;
	}

	void checkCurrent()
	{
		if (threadLocal.get() != this)
		{
			throw new IllegalStateException("SeleniumTestContext not associated with current thread.");
		}
	}

	synchronized void attach()
	{
		threadLocal.set(this);
		this.attachCount++;
	}

	synchronized void detach()
	{
		threadLocal.remove();
		this.attachCount--;
		if (this.attachCount <= 0)
		{
			this.webDriver.close();
			this.webDriver = null;
		}
	}
}
