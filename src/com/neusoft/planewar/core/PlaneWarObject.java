package com.neusoft.planewar.core;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.neusoft.planewar.client.PlaneWarClient;

public abstract class PlaneWarObject implements Drawable,Moveable{
	//实现画布和移动接口
	//创建飞机大战总父类
	public PlaneWarClient pwc;
	public int x;
	public int y;
	public Image img;
	public int width;
	public int height;
	public boolean good;

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, x, y, null);
		move();
	}

	//区分敌我飞机
	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}
	//抽象方法移动
	@Override
	public abstract void move();
	//创建画布：长宽坐标
	public Rectangle getRectangle(){
		return new Rectangle(x, y, width, height);
	}
}
