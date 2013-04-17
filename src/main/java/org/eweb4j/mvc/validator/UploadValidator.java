package org.eweb4j.mvc.validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.upload.UploadFile;
import org.eweb4j.util.CommonUtil;

/**
 * 对上传的文件验证
 * 
 * @author cfuture.aw
 * 
 */
public class UploadValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Context context) {
		Map<String, String> valError = new HashMap<String, String>();
		
		long countAllSize = 0;
		for (FieldConfigBean f : val.getField()) {
			String fieldName = f.getName();
			String msg = f.getMessage();
			
			List<UploadFile> files = context.getUploadMap().get(fieldName);
			if (files == null || files.isEmpty())
				continue;
			
			for (UploadFile file : files) {
				String fileName = file.getFileName();
				String fileContentType = file.getContentType();
				String tmpDir = null;
				long memoryMax = 0;
				String memoryMaxStr = null;
				long allSizeMax = 0;
				String allSizeMaxStr = null;
				long oneSizeMax = 0;
				String oneSizeMaxStr = null;
				String[] suffixArray = null;
				
				for (ParamConfigBean p : f.getParam()) {
					if (Validators.UPLOAD_TMP_DIR.equalsIgnoreCase(p.getName())) {
						tmpDir = p.getValue();
						continue;
					}
					
					if (Validators.UPLOAD_MAX_MEMORY_SIZE.equalsIgnoreCase(p.getName())) {
						memoryMax =  CommonUtil.strToInt(CommonUtil.parseFileSize(p.getValue())+"");
						memoryMaxStr = p.getValue();
						continue;
					}
					
					if (Validators.UPLOAD_MAX_REQ_SIZE.equalsIgnoreCase(p.getName())) {
						allSizeMax = CommonUtil.parseFileSize(p.getValue());
						allSizeMaxStr = p.getValue();
						continue;
					}
					
					if (Validators.UPLOAD_MAX_FILE_SIZE.equalsIgnoreCase(p.getName())) {
						oneSizeMax = CommonUtil.parseFileSize(p.getValue());
						oneSizeMaxStr = p.getValue();
						continue;
					}
					
					if (Validators.UPLOAD_SUFFIX.equalsIgnoreCase(p.getName())) {
						suffixArray = p.getValue().split(",");
						continue;
					}
				}
				
				if (suffixArray != null && suffixArray.length > 0) {
					boolean isOk = false;
					for (String suffix : suffixArray){
						if (fileName.endsWith("."+suffix)){
							isOk = true;
							break;
						}
					}
					
					if (!isOk){
						valError.put(fieldName, getParamValue(Validators.UPLOAD_SUFFIX_MSG, f.getParam()).replace("{suffix}", Arrays.asList(suffixArray).toString()));
						break;
					}
				}
				long fileSize = file.getSize();
				if (fileSize > oneSizeMax){
					valError.put(fieldName, getParamValue(Validators.UPLOAD_MAX_FILE_SIZE_MSG, f.getParam()).replace("{maxFileSize}", oneSizeMaxStr));
					break;
				}
				
				countAllSize += fileSize;
				if (countAllSize > allSizeMax){
					valError.put(fieldName, getParamValue(Validators.UPLOAD_MAX_REQ_SIZE_MSG, f.getParam()).replace("{maxRequestSize}", allSizeMaxStr));
					break;
				}
			}
		}
		
		Validation validation = new Validation();
		if (!valError.isEmpty())
			validation.getErrors().put(val.getName(), valError);
		
		return validation;
	}
	
	private String getParamValue(String name, List<ParamConfigBean> params){
		for (ParamConfigBean p : params) {
			if (name.equals(p.getName()))
				return p.getValue();
		}
		
		return null;
	}
}
