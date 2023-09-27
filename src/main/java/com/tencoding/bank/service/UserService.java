package com.tencoding.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tencoding.bank.dto.SignInFormDto;
import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.handler.exception.CustomRestfulException;
import com.tencoding.bank.repository.interfaces.UserRepository;
import com.tencoding.bank.repository.model.User;

@Service // IoC 대상 - 싱글톤 패턴
public class UserService {
	
	// DAO - 데이터 베이스 연동
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 회원 가입
	 * 비밀번호 암호화 처리
	 * @param signUpFormDto
	 */
	@Transactional
	public void signUp(SignUpFormDto signUpFormDto) {
		
		String rawPwd = signUpFormDto.getPassword();
		String hashPwd = passwordEncoder.encode(rawPwd);
		signUpFormDto.setPassword(hashPwd);
		int result = userRepository.insert(signUpFormDto);
		if (result != 1) {
			throw new CustomRestfulException("회원가입실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 로그인 서비스 처리
	public User signIn(SignInFormDto signInFormDto) {
		
		// 계정 이름만 확인으로 변경 처리
		User userEntity = userRepository.findByUsername(signInFormDto.getUsername());
		
		// 계정 확인
		if (userEntity == null || userEntity.getUsername().equals(signInFormDto.getUsername()) == false) {
			throw new CustomRestfulException("존재하지 않는 계정 입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 비밀번호 확인
		boolean isPwdMatched = passwordEncoder.matches(signInFormDto.getPassword(), userEntity.getPassword());
		
		if (isPwdMatched == false) {
			throw new CustomRestfulException("비밀번호가 틀렸습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return userEntity;
	}
	
	/**
	 * username 사용자 검색
	 * @param username
	 * @return User, null
	 */
	public User searchUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
