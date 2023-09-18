package com.tencoding.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.repository.model.User;

// ibatis -> 2.4 버전 이후로 MyBatis로 이름이 변경됨
@Mapper // Mapper 반드시 기술을 해주어야 동작한다.
public interface UserRepository {

	// 매개변수 수정 ( User user -> SignUpFormDto signUpFormDto )
	public int insert(SignUpFormDto signUpFormDto); 
	public int updateById(User user);
	public int deleteById(Integer id);
	public User findById(Integer id);
	public List<User> findAll();
	

}
