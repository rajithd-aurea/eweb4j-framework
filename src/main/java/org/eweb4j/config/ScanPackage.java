package org.eweb4j.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;

public abstract class ScanPackage {
	
	public Log log ;
	
	public ScanPackage(Log log){
		this.log = log;
	}
	
	public String readAnnotation(Collection<String> scans) {
		String error = null;

		try {
			if (scans == null)
				return error;

			Collection<String> classpaths = new HashSet<String>();
			Collection<File> jars = new HashSet<File>();
			Collection<String> paths = FileUtil.getClassPath();
			for (String path : paths){
				File f = new File(path);
				if (path.endsWith(".jar")){
					jars.add(f);
				}else{
					classpaths.add(path);
				}
			}
			
			for (String scan : scans) {

				if (scan == null || scan.length() == 0)
					continue;

				/* 扫描绝对路径 */
				if (scan.startsWith("AP:")) {
					classpaths.add(scan.replace("AP:", ""));
					scan = ".";
				} 
				
				// 扫描ClassPath中的*.class
				for (String classpath : classpaths)
					scanDir(scan, classpath, jars);
				
				// 扫描jar包
				scanJar(scan, jars);
				
			}

		} catch (Exception e) {
			error = StringUtil.getExceptionString(e);
			log.error(error);
		}

		return error;
	}

	private void scanDir(String scanPackage, String classDir, final Collection<File> jarCollect) throws Exception {
		File dir = null;
		if (".".equals(scanPackage)) {
			scanPackage = "";
			dir = new File(classDir);
		} else
			dir = new File(classDir + File.separator + scanPackage.replace(".", File.separator));

		log.debug("scan dir -> " + dir);
		// 递归文件目录
		if (dir.isDirectory())
			scanPackage(dir, scanPackage, jarCollect);
	}

	/**
	 * 扫描class文件
	 * 
	 * @param dir
	 * @param actionPackage
	 * @throws Exception
	 */
	private void scanPackage(File dir, String actionPackage, final Collection<File> jarCollect) throws Exception {
		if (!dir.isDirectory())
			return;

		File[] files = dir.listFiles();
		if (files == null || files.length == 0)
			return;
		for (File f : files) {

			if (f.isDirectory())
				if (actionPackage.length() == 0)
					scanPackage(f, f.getName(), jarCollect);
				else
					scanPackage(f, actionPackage + "." + f.getName(), jarCollect);

			else if (f.isFile()) {
				if (!f.getName().endsWith(".class")){
					if (f.getName().endsWith(".jar")){
						jarCollect.add(f);
					}
					
					continue;
				}

				StringBuilder sb = new StringBuilder(actionPackage);
				int endIndex = f.getName().lastIndexOf(".");

				String clsName = sb.append(".").append(f.getName().subSequence(0, endIndex)).toString();

				if (clsName == null || "".equals(clsName))
					continue;

				if (clsName.startsWith("."))
					clsName = clsName.substring(1);

				if (!handleClass(clsName))
					continue;
			}
		}
	}

	/**
	 * scan package by jars
	 * 
	 * @param jarsParentDirPath
	 * @param packageName
	 * @throws Exception
	 */
	private void scanJar(String packageName, Collection<File> ff) {
		if (ff == null)
			return;

		for (File f : ff) {
			ZipInputStream zin = null;
			ZipEntry entry = null;
			try {
				zin = new ZipInputStream(new FileInputStream(f));

				log.debug("scanning jar -> " + f.getAbsolutePath());

				while ((entry = zin.getNextEntry()) != null) {
					
					String entryName = entry.getName().replace('/', '.');
					if (".".equals(packageName)|| entryName.startsWith(packageName)){
						if (!entryName.endsWith(".class"))
							continue;
						
						final String className = entryName.replace(".class", "");
						if (className == null || className.trim().length() == 0)
							continue;
						try {
							if (!handleClass(className))
								continue;
						} catch (Error e) {
							continue;
						} catch (Exception e) {
							continue;
						}
					}

					zin.closeEntry();
				}
				zin.close();
			} catch (Error e) {
				continue;
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	protected abstract boolean handleClass(String className);
}
