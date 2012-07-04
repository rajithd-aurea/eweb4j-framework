package org.eweb4j.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eweb4j.mvc.config.ActionAnnotationConfig;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;

public abstract class ScanPackage {
	
	public Log log ;
	
	public ScanPackage(){
		log = LogFactory.getConfigLogger(getClass());
	}
	
	public String readAnnotation(List<String> scans) {
		String error = null;

		try {
			if (scans == null)
				return error;

			for (String scan : scans) {

				if (scan == null || scan.length() == 0)
					continue;

				/* 扫描绝对路径 */
				if (scan.startsWith("AP:")) {
					scanDir(".", scan.replace("AP:", ""));
				} else {
					// 扫描jar包
					String jarPath = FileUtil.getJarPath();
					scanJar(scan, jarPath);

					// 扫描ClassPath中的*.class
					String classDir = FileUtil.getTopClassPath(ActionAnnotationConfig.class);
					scanDir(scan, classDir);
				}
			}

		} catch (Exception e) {
			error = StringUtil.getExceptionString(e);
			log.error(error);
		}

		return error;
	}

	private void scanDir(String scanPackage, String classDir) throws Exception {
		File dir = null;
		if (".".equals(scanPackage)) {
			scanPackage = "";
			dir = new File(classDir);
		} else
			dir = new File(classDir + File.separator + scanPackage.replace(".", File.separator));

		log.debug("scan dir -> " + dir);
		// 递归文件目录
		if (dir.isDirectory())
			scanPackage(dir, scanPackage);
	}

	/**
	 * 扫描class文件
	 * 
	 * @param dir
	 * @param actionPackage
	 * @throws Exception
	 */
	private void scanPackage(File dir, String actionPackage) throws Exception {
		if (!dir.isDirectory())
			return;

		File[] files = dir.listFiles();
		if (files == null || files.length == 0)
			return;
		for (File f : files) {

			if (f.isDirectory())
				if (actionPackage.length() == 0)
					scanPackage(f, f.getName());
				else
					scanPackage(f, actionPackage + "." + f.getName());

			else if (f.isFile()) {
				if (!f.getName().endsWith(".class"))
					continue;

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
	private void scanJar(String packageName, String jarsParentDirPath) {
		String path = jarsParentDirPath;
		log.debug("scan " + path + " for jars");
		File[] ff = new File(path).listFiles();
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
