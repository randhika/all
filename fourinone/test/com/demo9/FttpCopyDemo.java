package com.demo9;

import com.fourinone.FttpAdapter;
import com.fourinone.FttpException;
import java.util.Date;

public class FttpCopyDemo
{
	public static void main(String[] args){
		try{
			long begin = (new Date()).getTime();
			FttpAdapter fromfile = new FttpAdapter("fttp://10.232.20.151/home/qianfeng.py/fttp/tmp/a.log");
			FttpAdapter tofile = fromfile.copyTo("fttp://10.232.20.154/home/qianfeng.py/fttp/tmp/a.log");
			if(tofile!=null)
				System.out.println("copy ok.");
			long end = (new Date()).getTime();
			System.out.println("time:"+(end-begin)/1000+"s");
		}catch(FttpException fe){
			fe.printStackTrace();
		}
	}
}