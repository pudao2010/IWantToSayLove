package com.bluewhaledt.saylove.ui.pay.entity;

import com.bluewhaledt.saylove.network.entities.BaseEntity;


public class PayCategory extends BaseEntity {

	/** 支付方式 (1:易联)(2:银联)(3:支付宝)(4:支付宝快捷支付)(5:微信支付) */
	public int payType;
	/** 标题 */
	public String title;
	/** 描述 */
	public String desc;

	/**默认选中哪种支付方式的控制字段*/
	public int defaultType;

	@Override
	public String[] uniqueKey() {
		return null;
	}

}
