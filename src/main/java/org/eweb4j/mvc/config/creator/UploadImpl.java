package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Upload;
import org.eweb4j.util.CommonUtil;


public class UploadImpl implements ValidatorCreator {

	private Upload ann;

	public UploadImpl(Upload ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.UPLOAD.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.UPLOAD);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		
		ParamConfigBean suffix = new ParamConfigBean();
		suffix.setName(Validators.UPLOAD_SUFFIX);
		StringBuilder sb = new StringBuilder();
		for (String s : ann.suffix()){
			if (sb.length() > 0)
				sb.append(",");
			sb.append(CommonUtil.parsePropValue(s));
		}
		suffix.setValue(sb.toString());
		fcb.getParam().add(suffix);
		
		ParamConfigBean suffixMsg = new ParamConfigBean();
		suffixMsg.setName(Validators.UPLOAD_SUFFIX_MSG);
		suffixMsg.setValue(CommonUtil.parsePropValue(ann.msg().suffix()));
		fcb.getParam().add(suffixMsg);
		
		ParamConfigBean maxFileSize = new ParamConfigBean();
		maxFileSize.setName(Validators.UPLOAD_MAX_FILE_SIZE);
		maxFileSize.setValue(CommonUtil.parsePropValue(ann.maxFileSize()));
		fcb.getParam().add(maxFileSize);
		
		ParamConfigBean maxFileSizeMsg = new ParamConfigBean();
		maxFileSizeMsg.setName(Validators.UPLOAD_MAX_FILE_SIZE_MSG);
		maxFileSizeMsg.setValue(CommonUtil.parsePropValue(ann.msg().maxFileSize()));
		fcb.getParam().add(maxFileSizeMsg);
		
		ParamConfigBean maxMemorySize = new ParamConfigBean();
		maxMemorySize.setName(Validators.UPLOAD_MAX_MEMORY_SIZE);
		maxMemorySize.setValue(CommonUtil.parsePropValue(ann.maxMemorySize()));
		fcb.getParam().add(maxMemorySize);
		
		ParamConfigBean maxMemorySizeMsg = new ParamConfigBean();
		maxMemorySizeMsg.setName(Validators.UPLOAD_MAX_MEMORY_SIZE_MSG);
		maxMemorySizeMsg.setValue(CommonUtil.parsePropValue(ann.msg().maxMemorySize()));
		fcb.getParam().add(maxMemorySizeMsg);
		
		ParamConfigBean maxRequestSize = new ParamConfigBean();
		maxRequestSize.setName(Validators.UPLOAD_MAX_REQ_SIZE);
		maxRequestSize.setValue(CommonUtil.parsePropValue(ann.maxRequestSize()));
		fcb.getParam().add(maxRequestSize);
		
		ParamConfigBean maxRequestSizeMsg = new ParamConfigBean();
		maxRequestSizeMsg.setName(Validators.UPLOAD_MAX_REQ_SIZE_MSG);
		maxRequestSizeMsg.setValue(CommonUtil.parsePropValue(ann.msg().maxRequestSize()));
		fcb.getParam().add(maxRequestSizeMsg);
		
		ParamConfigBean tmpDir = new ParamConfigBean();
		tmpDir.setName(Validators.UPLOAD_TMP_DIR);
		tmpDir.setValue(CommonUtil.parsePropValue(ann.tmpDir()));
		fcb.getParam().add(tmpDir);
		
		val.getField().add(fcb);
		return val;
	}

}
