package com.learning.ddd.onlinestore.commons.onetomany.spike2.ab;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ABService {

	@Autowired
	private ARepository aRepository;

	@Transactional
	public A createAandB(CreateABDTO createAandBkaDTO) {
		
		A a = new A();
		a.setName(createAandBkaDTO.getaName());
		
		B b = new B();
		b.setName(createAandBkaDTO.getbName());
		
		a.addB(b);
		
		return aRepository.save(a);
	}

	public List<A> getAandB() {
		return aRepository.findAll();
	}

	@Transactional
	public void deleteAandB(A a) {

		aRepository.delete(a);
	}
	
}
