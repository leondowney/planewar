package com.neusoft.planewar.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

import com.neusoft.planewar.client.PlaneWarClient;
import com.neusoft.planewar.constant.Constant;
import com.neusoft.planewar.util.ImageUtil;
import com.neusoft.planewar.util.MusicUtil;

public class Plane extends PlaneWarObject {
	public double speed = 10;// 我方飞机移动速度
	public boolean left, up, right, down;
	public int blood;// 飞机血量
	public int level;// 飞机等级
	public int type;// 飞机类型（区分敌我）
	public int score = 0;// 初始化分数


	public Plane() {
		super();
	}

	//初始化飞机相关属性（样式 位置 大小 血量 等级）
	public Plane(PlaneWarClient pwc, boolean good) {
		this.fire = false;
		this.x = (Constant.GAME_WIDTH - width) / 2;
		this.y = Constant.GAME_HEIGHT - height;
		this.img = ImageUtil.images.get("myPlane_01_01");
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
		this.pwc = pwc;
		this.good = good;
		this.blood = Constant.MYPLANE_MAX_BOOLD;
		this.level = 1;
		this.type = 1;
	}


	public Plane(int x, int y, String imageName) {
		super();
		this.x = x;
		this.y = y;
		this.img = ImageUtil.images.get(imageName);
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
	}

	public Plane(int x, int y, Image img) {
		super();
		this.x = x;
		this.y = y;
		this.img = img;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
	}

	/**
	 * �ж��ҷ��ɻ���������
	 */
	private void outOfBounds() {
		if (x <= 0-width/2)
			x = 0-width/2;
		if (x >= (Constant.GAME_WIDTH - width/2))
			x = Constant.GAME_WIDTH - width/2;
		if (y <= 0)
			y = 0;
		if (y >= (Constant.GAME_HEIGHT - height))
			y = Constant.GAME_HEIGHT - height;
	}

	/**
	 * �Ƿ񿪻�
	 */
	public boolean fire;

	/**
	 * �ҷ��ɻ����ӵ��ķ���
	 */
	public void fire() {
		// pwc.musics.add(mu);
		new MusicUtil("fire3").start();
		Missile missile = new Missile(pwc, this.x, this.y, "myPlane_missile_0" + type + "_0" + level, good);
		missile.x += (this.width - missile.width) / 2;
		missile.y -= height;
		pwc.missiles.add(missile);
	}

	boolean superFire;

	/**
	 * �����ӵ�
	 */
	public void superFire() {
		if (superFireCount > 0) {
			int num = 24;
			for (int i = 1; i <= num; i++) {
				Missile missile = new Missile(pwc, this.x, this.y, "myPlane_missile_super", 6, good);
				int r = (int) (Math.sqrt(width * width + height * height) / 2);
				int theta = 360 * i / num;
				missile.setTheta(theta);
				missile.x = (int) (missile.x + (width / 2 + r * Math.sin(Math.toRadians(theta)) - missile.width / 2));
				missile.y = (int) (missile.y
						- ((r * Math.cos(Math.toRadians(theta)) - height / 2 + missile.height / 2)));
				pwc.missiles.add(missile);
			}
			superFireCount--;
		}
		superFire = false;
	}

	/**
	 * �ж��Ƿ���
	 */
	public boolean live = true;

	@Override
	public void move() {
		if (left) {
			x -= speed;
		}
		if (right) {
			x += speed;
		}
		if (up) {
			y -= speed;
		}
		if (down) {
			y += speed;
		}
		outOfBounds();
		if (fire)
			fire();
		if (superFire)
			superFire();
	}

	@Override
	public void draw(Graphics g) {
		img = ImageUtil.images.get("myPlane_0" + type + "_0" + level);
		if (blood <= 0 && live) {
			live = false;
			Explode ex = new Explode(pwc, x, y);
			ex.x += (width - ex.width) / 2;
			ex.y += (height - ex.height) / 2;
			pwc.explodes.add(ex);
			pwc.enemyPlanes.clear();
			pwc.missiles.clear();
			pwc.items.clear();

		}

		if (live) {
			g.drawImage(img, x, y, null);
			move();
		}
		drawBloodAndScore(g);
	}

	/**
	 * ��Ѫ���ͻ���
	 * 
	 * @param g
	 */
	private void drawBloodAndScore(Graphics g) {
		Image bloodImg = ImageUtil.images.get("myBlood");
		Image blood_blank = ImageUtil.images.get("myBlood_blank");
		Image scoreImg = ImageUtil.images.get("score");
		int i = 0;
		g.drawImage(bloodImg, 10, 40, null);
		int num = (int) (((double) ((bloodImg.getWidth(null)) - 56) / (Constant.MYPLANE_MAX_BOOLD))
				* (Constant.MYPLANE_MAX_BOOLD - blood) / blood_blank.getWidth(null));
		for (int j = 0; j < num; j++) {
			g.drawImage(blood_blank, 10 + bloodImg.getWidth(null) - blood_blank.getWidth(null) * (j + 1), 40 + 14,
					null);
		}
		// ������
		g.drawImage(ImageUtil.images.get("score"), 10, 40 + bloodImg.getHeight(null) + 12, null);
		g.setFont(new Font("΢���ź�", Font.BOLD, 40));
		g.setColor(Color.WHITE);
		g.drawString(score + "", 10 + scoreImg.getWidth(null) + 10, 40 + bloodImg.getHeight(null) + 50);
	}

	/**
	 * ����ʣ�����
	 */
	public int superFireCount = 0;

	/**
	 * ���¼��̵ķ���
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			left = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_D:
			right = true;
			break;
		case KeyEvent.VK_W:
			up = true;
			break;
		case KeyEvent.VK_J:// ���ӵ�
			superFire = false;
			fire = true;
			break;
		case KeyEvent.VK_SPACE:// �������ӵ�
			fire = false;
			superFire = true;
			break;
		}
	}

	/**
	 * �ɿ����̵ķ���
	 * 
	 * @param e
	 *            �����¼�
	 */
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_J:// ���ӵ�
			fire = false;
			break;
		}
	}
}
