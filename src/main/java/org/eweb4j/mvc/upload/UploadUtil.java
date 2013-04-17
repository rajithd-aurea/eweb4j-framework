package org.eweb4j.mvc.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.UploadConfigBean;
import org.eweb4j.mvc.Context;
import org.eweb4j.util.CommonUtil;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-28 下午02:34:52
 */
public class UploadUtil {
	public static void handleUpload(Context context) throws Exception{
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class.getName());
		
		UploadConfigBean ucb = cb.getMvc().getUpload();
		String tmpDir = ucb.getTmp();
		int memoryMax = CommonUtil.strToInt(CommonUtil.parseFileSize(ucb.getMaxMemorySize())+"");
		long sizeMax = CommonUtil.parseFileSize(ucb.getMaxRequestSize());
		if (tmpDir.trim().length() == 0)
			tmpDir = "${RootPath}"+File.separator+"WEB-INF"+File.separator+"tmp";
		
		tmpDir = tmpDir.replace("${RootPath}", ConfigConstant.ROOT_PATH);
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(memoryMax);
		factory.setRepository(new File(tmpDir));
		
		ServletFileUpload _upload = new ServletFileUpload(factory);
		if (!_upload.isMultipartContent(context.getRequest()))
			return ;
		
		_upload.setSizeMax(sizeMax);
		
		try{
			List<FileItem> items = _upload.parseRequest(context.getRequest());
			
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()){
				FileItem item = it.next();
				String fieldName = item.getFieldName();
				if (item.isFormField()){
					String value = item.getString();
					context.getQueryParamMap().put(fieldName, new String[]{value});
				} else {
					String fileName = item.getName();
					if (fileName == null || fileName.trim().length() == 0)
						continue;
					
					String stamp = CommonUtil.getNowTime("yyyyMMddHHmmss");
					File tmpFile = new File(tmpDir + File.separator + stamp + "_" + fileName);
					item.write(tmpFile);
					
					UploadFile uploadFile = new UploadFile(tmpFile, fileName, fieldName, item.getSize(), item.getContentType());
					
					if (context.getUploadMap().containsKey(fieldName)){
						context.getUploadMap().get(fieldName).add(uploadFile);
					}else{
						List<UploadFile> uploads = new ArrayList<UploadFile>();
						uploads.add(uploadFile);
						context.getUploadMap().put(fieldName, uploads);
					}
				}
			}
		}catch(InvalidContentTypeException e){
			throw new Exception("upload file error", e);
		} 
	}
}
