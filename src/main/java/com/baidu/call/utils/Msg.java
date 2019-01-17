package com.baidu.call.utils;

public class Msg {
	private boolean success = false;
	
	private boolean hasAuth=true;

	private String msg = "";

	private Object obj = null;
	
	private String redirect="";
	
	private String percent="";
	
	private String cacheKey="";

	public Msg() {
		super();
	}


	public Msg(boolean success, String msg, String cacheKey) {
		super();
		this.success = success;
		this.msg = msg;
		this.cacheKey = cacheKey;
	}


	public Msg(String msg, String percent) {
		super();
		this.msg = msg;
		this.percent = percent;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}


	public Msg(boolean success) {
		super();
		this.success = success;
	}

	public Msg(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}


	public Msg(boolean success, String msg, Object obj) {
		super();
		this.success = success;
		this.msg = msg;
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isHasAuth() {
		return hasAuth;
	}

	public void setHasAuth(boolean hasAuth) {
		this.hasAuth = hasAuth;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
}
