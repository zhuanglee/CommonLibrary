package cn.lzh.commonlibrary.bean;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * Activity信息
 * @author lzh
 *
 */
public class ActvityBean implements Serializable, Comparable<ActvityBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name="";
	private String packageName = "";
	private String className = "";
	private String label = "";
	/**
	 * 图标iconResId|iconUrl|icon
	 */
	private Object icon;
	
	public ActvityBean() {
	}

	public ActvityBean(String label) {
		this.label = label;
	}

	/**
	 * 根据反射获取Activity信息
	 * @param clazz Activity类
	 */
	public ActvityBean(Class<?> clazz) {
		if(clazz!=null){
			name=clazz.getName();
			Package packageObj = clazz.getPackage();
			packageName=packageObj.getName();
			className=clazz.getSimpleName();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLabel() {
		if (TextUtils.isEmpty(label)) {
			label = className;
		}
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Object getIcon() {
		return icon;
	}

	public void setIcon(Object icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "ActvityBean [name=" + name + ", packageName=" + packageName
				+ ", className=" + className + ", label=" + label + ", icon="
				+ icon + "]";
	}

	@Override
	public int compareTo(ActvityBean another) {
		return getName().compareTo(another.getName());
	}

}
