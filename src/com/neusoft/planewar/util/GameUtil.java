package com.neusoft.planewar.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import javazoom.jl.player.Player;

/**
 * ��ȡ��ǰ��Ŀ�и�����Դ
 * @author zzk
 *
 */
public class GameUtil {
	/**
	 * ����ͼƬ�����·����ȡͼƬ
	 * @param imagePath
	 * @return ͼƬ
	 */
	public static Image getImage(String imagePath){
		URL url = GameUtil.class.getClassLoader().getResource(imagePath);
		BufferedImage img = null;
		try {
			img=ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(url);
		}
		return img;
	}
}
