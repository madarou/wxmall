package com.makao.entity;
/**
 * @description: TODO
 * @author makao
 * @date 2016年5月20日
 * 产品所属的分类，每个Area下有几个分类，直接以'名称1=排序值1,名称2=排序值2'的形式存在Area表中，
 * 所以Catalog并不单独建表
 */
public class Catalog {
	private String name;
	private String sequence;//排序
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}
