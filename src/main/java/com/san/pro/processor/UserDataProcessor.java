package com.san.pro.processor;

import org.springframework.batch.item.ItemProcessor;

import com.san.pro.model.User;

public class UserDataProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User user) throws Exception {
		return user;
	}
	
}
