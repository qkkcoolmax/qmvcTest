package com.qmvc.test;

import com.qmvc.annotation.TransactionMethod;

public class TransController implements ITransController {

	@Override
	@TransactionMethod
	public void testTran() {
		Member member = new Member();
		member.save();

	}

}
