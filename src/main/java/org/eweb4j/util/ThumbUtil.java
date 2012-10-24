package org.eweb4j.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 缩略图工具类
 * @author weiwei
 *
 */
public class ThumbUtil {

	/**
	 * 注意！当宽度和高度都给定的情况下会进行裁剪。裁剪规则是：先按照比例压缩，然后将多出的部分分两边裁剪。
	 * @param remoteImageUrl 远程图片URL
	 * @param outputFormat 希望生成的缩略图格式
	 * @param failRetryTimes 远程图片下载失败尝试次数
	 * @param sleep 重试间隔时间 单位 毫秒
	 * @param outputWidth 希望生成的缩略图宽度
	 * @param outputHeight 希望生成的缩略图高度
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream generateThumb(String remoteImageUrl, String outputFormat, int failRetryTimes, long sleep, int outputWidth, int outputHeight) throws Exception{
		if (remoteImageUrl == null || remoteImageUrl.trim().length() == 0)
			throw new Exception("ImageURL required");
		
		if (outputFormat == null || outputFormat.trim().length() == 0)
			outputFormat = remoteImageUrl.substring(remoteImageUrl.lastIndexOf(".")+1, remoteImageUrl.length());
		
		if (outputFormat == null || outputFormat.trim().length() == 0)
			throw new Exception("can not get the image suffix -> " + remoteImageUrl);
		
		if (failRetryTimes <= 0)
			failRetryTimes = 1;
		
		if (outputWidth <= 0 && outputHeight <= 0)
			throw new Exception("outputWidth and outputHeight must have one");
		
		final String W = "width";
		final String H = "height";
		//原图宽高
		final Map<String,Integer> source = new HashMap<String,Integer>();
		//目标宽高
		final Map<String,Integer> output = new HashMap<String, Integer>();
		
		if (outputWidth > 0)
			output.put(W, outputWidth);
		
		if (outputHeight > 0)
			output.put(H, outputHeight);
		
		BufferedImage bi = null;
		try {
			bi = FileUtil.getBufferedImage(remoteImageUrl, failRetryTimes, sleep);
		}catch(Exception e){
			throw e;
		}
		
		if (bi == null)
			throw new Exception("can not get the image from website");
		
		//比较W与H，找出小的，记住小的那个
		int w = bi.getWidth();
		int h = bi.getHeight();
		source.put(W, w);
		source.put(H, h);
		
		String min = W;
		if (h < w)
			min = H;
		// 如果小值不存在，则小值取给过来的其中一个值
		if (!output.containsKey(min)){
			if (output.containsKey(W))
				min = W;
			else
				min = H;
		}
		
		//算出比例
		double scale = (double)source.get(min)/output.get(min);
		int sW = new Double(w/scale).intValue();
		int sH = new Double(h/scale).intValue();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		//如果给了两个参数，则剪裁
		if (output.containsKey(W) && output.containsKey(H)){
			//压缩
			BufferedImage _bi = Thumbnails.of(bi).size(sW, sH).outputFormat(outputFormat).asBufferedImage();
			//scale必须为 1 的时候图片才不被放大
			Thumbnails.of(_bi).scale(1).sourceRegion(Positions.CENTER, output.get(W), output.get(H)).outputFormat(outputFormat).toOutputStream(os);
			
		}else{
			//压缩
			Thumbnails.of(bi).size(sW, sH).outputFormat(outputFormat).toOutputStream(os);
		}
		
		return os;
	}
	
	public static void main(String[] args) throws Exception{
		String outputFormat = "jpg";
		String remoteImageUrl = "http://static.deal.com.sg/sites/default/files/BodySlimmingMassager.jpg";
		int outputWidth = 1000;
		int outputHeight = 1000;
		
		File file = new File("d:/test_w"+outputWidth+"h"+outputHeight+".jpg");
		
		ByteArrayOutputStream os = ThumbUtil.generateThumb(remoteImageUrl, outputFormat, 1, 1*1000, outputWidth, outputHeight);
		FileOutputStream writer = new FileOutputStream(file);
		writer.write(os.toByteArray());
		File _f = new File(file.getAbsolutePath());
		
		System.out.println("generate file -> " + _f.getAbsolutePath() + " " + _f.exists());
	}
	
}
