package com.music.player.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * activity基类
 *
 */
public abstract class BaseActivity extends Activity {

	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(savedInstanceState);
		findView();
		initView();
		initData();
		initListener();
	}


	/**
	 * 查找view
	 *
	 * @createDate 2016-3-8
	 * @createTime 下午4:13:53
	 * @auther lcb
	 */
	public abstract void setContentView(Bundle savedInstanceState);


	/**
	 * 查找view
	 *
	 * @createDate 2016-3-8
	 * @createTime 下午4:13:53
	 * @auther lcb
	 */
	public abstract void findView();

	/**
	 * 初始化view
	 *
	 * @createDate 2016-3-8
	 * @createTime 下午4:14:05
	 * @auther lcb
	 */
	public abstract void initView();

	/**
	 * 初始化数据
	 *
	 * @createDate 2016-3-8
	 * @createTime 下午4:14:13
	 * @auther lcb
	 */
	public abstract void initData();

	/**
	 * 初始化监听器
	 *
	 * @createDate 2016-3-8
	 * @createTime 下午4:14:13
	 * @auther lcb
	 */
	public abstract void initListener();


}
