package com.MissX.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;
/**
 * 2018年9月9日 下午4:43:47
 * @author H
 * TODO 保存图片的工具
 * Admin
 */
@Component
public class FileSaveUtil {
	
	public String ROOT;
	public String NAME;
	/**
	 * 构造方法，初始化保存路径，若配置文件未定义则保存到D:\WebFile\Miss-X\
	 * @param TestRoot
	 */
	public FileSaveUtil(String Root,String name) {
		ROOT = Root;
		NAME = name;
	}
	public FileSaveUtil() {
		ROOT = "D:\\WebFile\\";
		NAME = "Miss-X";
	}
	/**
	 * 保存图片-不压缩.返回文件路径
	 * @param mFile 传入的文件流
	 * @return	返回文件的保存路径
	 * @throws IOException
	 */
	public String SaveIMG(MultipartFile mFile) throws IOException {
		Date date = new Date();
		String path = ROOT + NAME +"/"+ new SimpleDateFormat("YYYY-MM-dd_").format(date);
		String fileName = new SimpleDateFormat("HH-mm-ss-").format(date);
		File fold = new File(path);
		try {
			if(!fold.exists()) {
				fold.mkdirs();
			}
			fileName+=new Date().getTime();
			String originalFilename = mFile.getOriginalFilename();
			int last = originalFilename.lastIndexOf(".");
			int length = originalFilename.length();
			try {
				fileName+=originalFilename.substring(last,length);
			}catch (StringIndexOutOfBoundsException e) {
				throw new IOException("没有文件");
			}
			File file = new File(path+"/"+fileName);
			writeFile(mFile, file);
			path = file.getPath().replace(ROOT, "\\file\\");
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("文件保存出错");
			return null;
		}
		System.out.println("文件路径: "+path);
		return path.replace("\\", "/");
	};
	
	/**
	 * 保存图片-压缩至1/2.返回文件路径
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	public String SaveIMG_5(MultipartFile mFile) throws IOException {
		Date date = new Date();
		String path = ROOT + NAME +"/"+ new SimpleDateFormat("YYYY-MM-dd_").format(date);
		String fileName = new SimpleDateFormat("HH-mm-ss-").format(date);
		File fold = new File(path);
		try {
			if(!fold.exists()) {
				fold.mkdirs();
			}
			fileName += new Date().getTime();
			String originalFilename = mFile.getOriginalFilename();
			int last = originalFilename.lastIndexOf(".");
			int length = originalFilename.length();
			try {
				fileName += originalFilename.substring(last,length);
			}catch (StringIndexOutOfBoundsException e) {
				throw new IOException("没有文件");
			}
			File file = new File(path+"/"+fileName);
			writeFile(mFile, file);
			path = file.getPath().replace(ROOT, "\\file\\");
			Thumbnails.of(file).scale(1f).outputQuality(0.5f).toFile(file);
		}catch(IOException e) {
			System.out.println("文件保存出错");
			return null;
		}
		System.out.println("文件路径: "+path);
		return path.replace("\\", "/");
	}
	
	/**
	 * 写入文件
	 * @param mFile
	 * @param file
	 * @throws IOException
	 */
	private static void writeFile(MultipartFile mFile,File file) throws IOException {
		InputStream is = mFile.getInputStream();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		byte [] b = new byte[1024];
		while(bis.read(b)!=-1) {
			bos.write(b);
		}
		bos.flush();
		bos.close();
		changeIMG(file,file);
	}
	
	/**
	 * 根据地址获取名字
	 * @param path
	 * @return
	 */
	public static String getNameForPath(String path){
		String fileName = "/file"+path.split("file")[1];
		System.out.println("文件名为: "+fileName);
		return fileName;
	}
	
	/**
	 * 压缩图片
	 * @param file  	原路径
	 * @param file1		新路径
	 * @throws IOException
	 */
	public static void changeIMG(File file,File file1) throws IOException {
		Thumbnails.of(file).scale(1f).outputQuality(0.25f).toFile(file1);
	}
}
