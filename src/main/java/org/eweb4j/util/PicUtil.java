package org.eweb4j.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class PicUtil {
	
	public static void main(String[] args) throws Exception{
		String url = "http://beautydeals.sg/components/com_enmasse/upload/438081254ladies_watch_main - copy.jpg";
		PicsModel model = getAttrs(url);
		BufferedImage img = FileUtil.getBufferedImage(url, 3, 1*1000);
		System.out.println(img.getWidth());
		// "E://hekai//hk0001.jpg" 也是可以的
		System.out.println("picextendname is:" + model.getExtName());
		System.out.println("picwidth is:" + model.getWidth());
		System.out.println("picheight is:" + model.getHeight());
		System.out.println("piccolor:" + model.getColor());
		System.out.println("picsize:" + model.getSize());
	}

	public static PicsModel getAttrs(String picpath) throws Exception{
		PicsModel model = null;
		String picextendname = null;
		byte[] content = readFromFile(picpath);
		int k = content.length;
		if (k == 0)
			return model;
		// 不想处理的话，请直接获取其字节数
		Integer kk = null;
		String picsize = null;
		if (k >= 1024) {
			// bigger than fact pic file sizes
			k = k / 1024 + 1;
			kk = new Integer(k);
			picsize = kk.toString() + "K";
		} else if (k > 0) {
			kk = new Integer(k);
			picsize = kk.toString();
		}
		model = new PicsModel();
		model.setSize(picsize);

		picextendname = getFileExtendName(content);
		if (picextendname == null || picextendname.trim().length() == 0)
			return model;
		
		int picwidth = 0;
		int picheight = 0; 
		int color = 0;
		String piccolor = null;

		if (picextendname.equals("GIF")) {
			// picwidth position
			picwidth = getFileAttribute(content, 7, 2, picextendname);
			// picheight position
			picheight = getFileAttribute(content, 9, 2, picextendname);
			// piccolor position
			color = getFileAttribute(content, 10, 1, picextendname);
			color = color % 8 + 1;
			piccolor = getPicColor(color);
		}
		if (picextendname.equals("JPG")) {
			// 考虑了两种情况
			picwidth = getFileAttribute(content, 166, 2, picextendname);
			picheight = getFileAttribute(content, 164, 2, picextendname);
			color = getFileAttribute(content, 167, 1, picextendname);
			color = color * 8;
			if ((picwidth == 0) || (picheight == 0) || (color > 3)) {
				picwidth = getFileAttribute(content, 197, 2, picextendname);
				picheight = getFileAttribute(content, 195, 2, picextendname);
				color = getFileAttribute(content, 198, 1, picextendname);
				color = color * 8;
			}
			piccolor = getPicColor(color);
		}
		if (picextendname.equals("BMP")) {
			picwidth = getFileAttribute(content, 19, 2, picextendname);
			picheight = getFileAttribute(content, 23, 2, picextendname);
			color = getFileAttribute(content, 28, 1, picextendname);
			piccolor = getPicColor(color);
		}
		if (picextendname.equals("PNG")) {
			picwidth = getFileAttribute(content, 19, 2, picextendname);
			picheight = getFileAttribute(content, 23, 2, picextendname);
			// usually is "16M"??
			piccolor = "16M";
		}

		model.setExtName(picextendname);
		model.setWidth(picwidth);
		model.setHeight(picheight);
		model.setColor(piccolor);

		return model;
	}

	private static byte[] readFromFile(String fileName) throws IOException{
		InputStream is = null;
		try {
			File file = new File(fileName);
			
			if (!file.exists()){
				URL host = new URL(fileName);
				URI uri = new URI(
					    host.getProtocol(), 
					    host.getHost(), 
					    host.getPath(),
					    null);
				URL url = uri.toURL();
				is = url.openStream();
			}else{
				is = new FileInputStream(fileName);
			}
			
			byte[] buf = new byte[is.available()];
			is.read(buf);
			return buf;
		}catch (IOException e){
			throw e;
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}finally{
			if (is != null)
				is.close();
		}
	}

	private static String getFileExtendName(byte[] byte1) {
		String strFileExtendName = null;

		// header bytes contains GIF87a or GIF89a?
		if ((byte1[0] == 71) && (byte1[1] == 73) && (byte1[2] == 70)
				&& (byte1[3] == 56) && ((byte1[4] == 55) || (byte1[4] == 57))
				&& (byte1[5] == 97)) {
			strFileExtendName = "GIF";
		}
		// header bytes contains JFIF?
		if ((byte1[6] == 74) && (byte1[7] == 70) && (byte1[8] == 73)
				&& (byte1[9] == 70)) {
			strFileExtendName = "JPG";
		}
		// header bytes contains BM?
		if ((byte1[0] == 66) && (byte1[1] == 77)) {
			strFileExtendName = "BMP";
		}
		// header bytes contains PNG?
		if ((byte1[1] == 80) && (byte1[2] == 78) && (byte1[3] == 71)) {
			strFileExtendName = "PNG";
		}

		return strFileExtendName;
	}

	private static int getFileAttribute(byte[] byte2, int n, int m,
			String fileextendname) {
		int j, FileAttributeValue;
		j = 0;
		FileAttributeValue = 0;
		String str, str1;
		str = "";
		str1 = "";

		// 如果其大于127，则反之出现少于0，需要进行＋256运算
		for (int k = 0; k < m; k++) {
			if (byte2[n - k] < 0) {
				j = byte2[n - k];
				j = j + 256;
			} else {
				j = byte2[n - k];
			}

			str1 = Integer.toHexString(j);
			// 转化为16进制，不足位补0
			if (str1.length() < 2) {
				str1 = "0" + str1;
			}

			// 格式的不同，表达属性的字节也有变化
			if (fileextendname.equalsIgnoreCase("JPG")
					|| fileextendname.equalsIgnoreCase("PNG")) {
				str = str1 + str;
			} else {
				str = str + str1;
			}
		}

		FileAttributeValue = HexToDec(str);
		return FileAttributeValue;
	}

	private static int HexToDec(String cadhex) {
		int n, i, j, k, decimal;
		String CADHEX1;
		n = 0;
		i = 0;
		j = 0;
		k = 0;
		decimal = 0;
		CADHEX1 = null;
		n = cadhex.length();
		CADHEX1 = cadhex.trim().toUpperCase();

		while (i < n) {
			j = CADHEX1.charAt(i);
			if ((j >= 48) && (j < 65)) {
				j = j - 48;
			}
			if (j >= 65) {
				j = j - 55;
			}
			i = i + 1;

			// 16幂运算
			k = 1;
			for (int m = 0; m < (n - i); m++) {
				k = 16 * k;
			}
			decimal = j * k + decimal;
		}

		return decimal;
	}

	private static String getPicColor(int color) {
		int k;
		k = 1;
		String piccolor;
		piccolor = null;
		// 2幂运算
		for (int m = 0; m < color; m++) {
			k = 2 * k;
		}

		Integer kk;
		kk = null;
		if (k >= 1048576) {
			k = k / 1048576;
			kk = new Integer(k);
			piccolor = kk.toString() + "M";
		} else if (k >= 1024) {
			k = k / 1024;
			kk = new Integer(k);
			piccolor = kk.toString() + "K";
		} else if (k > 0) {
			kk = new Integer(k);
			piccolor = kk.toString();
		}

		return piccolor;
	}

}