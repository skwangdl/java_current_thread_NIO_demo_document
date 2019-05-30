package com.yj.controller;

import com.yj.service.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BizController {

	@Autowired
	private BizService bizService;
	
	@RequestMapping("/checkOut")
	public String checkOut() {
		try {
			bizService.checkOut(1,1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
