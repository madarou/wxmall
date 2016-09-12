package com.makao.entity;

public enum OrderState {
	NOT_PAID("未支付",1),
	QUEUE("排队中",2),
	PROCESS_WAITING("待处理",3),
	DISTRIBUTING("配送中",4),
	DISTRIBUTED("已配送",5),
	RECEIVED("已收货",6),
	CANCELED("已取消",7),
	RETURN_APPLYING("退货申请中",8),
	RETURNING("退货中",9),
	RETURNED("已退货",10),
	RETURN_CANCELED("已取消退货",11),
	REFUNDED("已退款",12);
	
	private String text;
    private int code;

    // 构造方法
    private OrderState(String text, int code) {
        this.text = text;
        this.code = code;
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
