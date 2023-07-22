package com.example.demo.service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service("otpserv")
public class OtpServImpl implements OtpService {

	private static final Integer EXPIRE_MINS=1;
	
	private LoadingCache<String, Integer> otpcache;
	
	
	public OtpServImpl() {
		super();
		
		otpcache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {

			@Override
			public Integer load(String key) throws Exception {
				// TODO Auto-generated method stub
				return 0;
			}
		});
	}
	
	@Override
	public int generateotp(String uname) {
		// TODO Auto-generated method stub
		
		Random rand = new Random();
		
		int otp = 100000 + rand.nextInt(900000);
		System.out.println("\n In the otpservice layer OTP is "+otp);
		
		otpcache.put(uname, otp);
		
		return 0;
	}

	@Override
	public int getOtp(String uname) {
		// TODO Auto-generated method stub
		try {
			return otpcache.get(uname);
		}
		catch(ExecutionException e){
			return 0;
		}
	}

	@Override
	public void clearOtp(String uname) {
		// TODO Auto-generated method stub
		otpcache.invalidate(uname);
	}

}
