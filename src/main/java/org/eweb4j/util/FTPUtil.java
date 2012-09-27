package org.eweb4j.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtil implements ProtocolCommandListener{
	
	public FTPClient client = new FTPClient();
	private String host;
	private int port;
	private String user;
	private String pwd;
	private FTPListener listener;
	private boolean debug;
	
	public FTPUtil(String host, int port, String user, String pwd) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
	}

	public void setListener(FTPListener listener){
		this.listener = listener;
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	public void connectAndLogin() throws Exception{
		client.addProtocolCommandListener(this);
		client.connect(host, port); 
		client.setControlEncoding("UTF-8"); 
		client.setFileType(FTPClient.BINARY_FILE_TYPE);
        if(!FTPReply.isPositiveCompletion(client.getReplyCode())){ 
        	client.disconnect();
        	throw new Exception("FTP is not PositiveCompletion");
        }
        
        if(!client.login(user, pwd)){
        	client.disconnect();
        	throw new Exception("FTP login fail");
        }
        
        if (listener != null)
        	listener.onInfo("FTP login success");
	}
	
    /**
    * 递归创建远程服务器目录
    * 
    * @param remote
    *            远程服务器文件绝对路径
    * 
    * @return 目录创建是否成功
    * @throws IOException
    */
    public boolean makeDirectory(String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !client.changeWorkingDirectory(new String(directory))) {

            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end));
                if (!client.changeWorkingDirectory(subDirectory)) {
                    if (client.makeDirectory(subDirectory)) {
                    	client.changeWorkingDirectory(subDirectory);
                    } else {
                    	if (this.listener != null)
                    		listener.onInfo("makie dir -> " + subDirectory + " fail");
                        success = false;
                        return success;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        
        return success;
    }
    
    public void protocolCommandSent(ProtocolCommandEvent ev) {
		try {
			String info  = new String(("sent->"+ev.getReplyCode()+"-"+"c->"+ev.getCommand()+"-"+ev.getMessage()).getBytes("iso8859-1"), "gbk");
			if (this.debug && listener != null)
				listener.onInfo(info);
		} catch (UnsupportedEncodingException e) {
			String info = e.toString();
			if (listener != null)
				listener.onError(info, e);
		}
	}

	public void protocolReplyReceived(ProtocolCommandEvent ev) {
		try {
			String info  = new String(("receive->"+ev.getReplyCode()+"-"+"c->"+ev.getCommand()+"-"+ev.getMessage()).getBytes("iso8859-1"), "gbk");
			if (this.debug && listener != null)
				listener.onInfo(info);
		} catch (UnsupportedEncodingException e) {
			String info = e.toString();
			if (listener != null)
				listener.onError(info, e);
		}
	}
}
