package org.eweb4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;

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
//        if(!FTPReply.isPositiveCompletion(client.getReplyCode())){ 
//        	client.disconnect();
//        	throw new Exception("FTP is not PositiveCompletion");
//        }
        
        if(!client.login(user, pwd)){
        	client.disconnect();
        	throw new Exception("FTP login fail");
        }
        
        if (listener != null)
        	listener.onInfo("FTP login success");
	}
	
	public void logoutAndDisconnect(){
		if (client != null) {
			try {
				client.logout();
				if (listener != null)
					listener.onInfo(" ftp logout success");
			} catch (Exception e){
				if (listener != null)
					listener.onError(" can not logout ftp", e);
			}
			
			try {
				client.disconnect();
				if (listener != null)
					listener.onInfo(" ftp disconnect success");
			} catch (Exception e){
				if (listener != null)
					listener.onError(" can not disconnect ftp", e);
			}
		}
	}
	
	private static String resolvePath(String path){
		File dir = new File(path);
		return dir.getPath().replace("\\", "/");
	}
	
	public boolean mkdir(String remoteDir) throws Exception{
		if (remoteDir == null || remoteDir.trim().length() == 0)
			throw new Exception("remoteDir -> " + remoteDir + " required !");
		String path = FTPUtil.resolvePath(remoteDir);
		// split by /
		String[] remoteDirs = path.split("/");
		StringBuilder builder = new StringBuilder("/");
		for (String dir : remoteDirs){
			if (builder.length() > 1)
				builder.append("/");
			
			builder.append(dir);
			boolean isDirOk = client.changeWorkingDirectory(builder.toString());
			if (!isDirOk)
				isDirOk = client.makeDirectory(builder.toString());
			
			if (!isDirOk)
				throw new Exception("ftp mkdir -> " + builder.toString() + " fail");
		}
		
		return true;
	}
	
	public static void main(String[] args){
//		FTPUtil ftp = new FTPUtil("122.248.240.88", 21, "oldweb", "123654");
//		FTPUtil ftp = new FTPUtil("54.251.32.238", 21, "myilove", "24abcdef");
		FTPUtil ftp = new FTPUtil("sytime.com", 21, "wfl", "wufulin");
		try {
			
			ftp.client.setDataTimeout(10*60*1000);
			ftp.client.setConnectTimeout(60*60*1000);
			ftp.setDebug(true);
			ftp.setListener(new FTPListener() {
				public void onInfo(String info) {
					System.out.println(info);
				}
				
				public void onError(String err, Exception e) {
					e.printStackTrace();
				}
			});
		
			ftp.connectAndLogin();
			//PASSIVE_REMOTE_DATA_CONNECTION_MODE
			ftp.client.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
			
//			String remote = "/var/www/html/files/deals/watch3.jpg";
			String remote = "/wwwroot/longyan-web/cache/store/watch.jpg";
//			String remote = "/wwwroot/myilovedeals/cache/watch.jpg";
			
			String local = "d:/test_facebook.jpg";
			File f = new File(local);
			InputStream is = new FileInputStream(f);
			boolean isOk = ftp.client.storeFile(remote, is);
			System.out.println("fuck-->"+isOk);
			ftp.client.logout();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (ftp != null && ftp.client != null) {
				if (ftp.client.isConnected()) {
					try {
						ftp.client.disconnect();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}
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
