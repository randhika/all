package com.fourinone;

import java.util.Date;
import java.util.concurrent.TimeoutException;

public class StartResult<E> extends Result{
	private Process p;
	private long s;
	
	public StartResult(){
		super();
	}
	
	public StartResult(boolean ready)
	{
		super(ready);
	}
	
	public StartResult(Process p, boolean ready){
		this(ready);
		this.p = p;
		s = (new Date()).getTime();
	}
	
	public int getStatus()
	{
		try{
			//System.out.println("p.exitValue():"+p.exitValue());
			setResult(p.exitValue(),p.exitValue()==0?READY:EXCEPTION);
		}catch(IllegalThreadStateException ex){
			//status = NOTREADY;
		}
		return status;
	}
	
	public int getStatus(long timeout){
		if((new Date()).getTime()-s>timeout){
			TimeoutException te = new TimeoutException("TryStart StartResult Timeout");
			LogUtil.info("[TryStart]", "[Timeout]", te.getMessage()+" and be killed");
			kill(EXCEPTION);
			//System.out.println("kill status:"+status);
			return status;
		}else return getStatus();
	}
	
	public void kill(){
		kill(READY);
	}
	
	private void kill(int status){
		p.destroy();
		setResult(1,status);
	}
	
	void setResult(int res, int status)
	{
		setResult(res);
		setReady(status);
	}
	
	public static long h(long t){
		return m(t*60l);
	}
	
	public static long m(long t){
		return s(t*60l);
	}
	
	public static long s(long t){
		return t*1000l;
	}
}