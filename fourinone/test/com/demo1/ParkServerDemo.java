package com.demo1;

import com.fourinone.BeanContext;

public class ParkServerDemo
{
	public static void main(String[] args)
	{
		BeanContext.setConfigFile("/Users/start/Documents/Developer/eclipse/fourinone/src/config.xml");
		BeanContext.startPark();
	}
}