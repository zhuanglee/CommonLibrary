package cn.lzh.common.bean;


import cn.lzh.common.utils.PinyinUtils;

public class GoodMan implements Comparable<GoodMan>{

	private String name;
	private String pinyin;
	
	public GoodMan(String name) {
		this.name=name;
		this.pinyin= PinyinUtils.getPinYin(name);
	}
	
	public String getName() {
		return name;
	}

	public String getPinyin() {
		return pinyin;
	}

	@Override
	public int compareTo(GoodMan another) {
		return pinyin.compareTo(another.getPinyin());
	}

}
