package com.fourinone;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;

class PoolExector
{
	private static ThreadPoolExecutor tpe;
	
	static ThreadPoolExecutor tpe()
	{
		if(tpe==null)
		{
			int corePoolSize = ConfigContext.getInitServices();
			int maximumPoolSize = ConfigContext.getMaxServices();
			long keepAliveTime = 3000;
			TimeUnit unit = TimeUnit.MILLISECONDS;
			BlockingQueue<Runnable> waitQueue = new ArrayBlockingQueue<Runnable>(2000);
			RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();//ThreadPoolExecutor.CallerRunsPolicy();
			tpe =new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, waitQueue, handler);
		}
		return tpe;
	}
	
	static void close(){
		if(tpe!=null){
			try{
				tpe.shutdown();
				tpe=null;
			}catch(SecurityException se){
				LogUtil.info("[tpe]", "[close]", "[Error Exception:]", se);
			}
		}
	}
}