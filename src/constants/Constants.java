package constants;

import java.util.HashMap;

public class Constants {
	public static final HashMap<Integer, Integer> GRAVITY;
	static {
		GRAVITY = new HashMap<Integer, Integer>();
		GRAVITY.put(0, 4);
		GRAVITY.put(30, 6);
		GRAVITY.put(35, 8);
		GRAVITY.put(40, 10);
		GRAVITY.put(50, 12);
		GRAVITY.put(60, 16);
		GRAVITY.put(70, 32);
		GRAVITY.put(80, 48);
		GRAVITY.put(90, 64);
		GRAVITY.put(100, 80);
		GRAVITY.put(120, 96);
		GRAVITY.put(140, 112);
		GRAVITY.put(160, 128);
		GRAVITY.put(170, 144);
		GRAVITY.put(200, 4);
		GRAVITY.put(220, 32);
		GRAVITY.put(230, 64);
		GRAVITY.put(233, 96);
		GRAVITY.put(236, 128);
		GRAVITY.put(239, 160);
		GRAVITY.put(243, 192);
		GRAVITY.put(247, 224);
		GRAVITY.put(251, 256); //1G
		GRAVITY.put(300, 512); //2G
		GRAVITY.put(330, 768); //3G
		GRAVITY.put(360, 1024); //4G
		GRAVITY.put(400, 1280); //5G
		GRAVITY.put(420, 1024); //4G
		GRAVITY.put(450, 768); //3G
		GRAVITY.put(500, 5120); //20G
	}
}
