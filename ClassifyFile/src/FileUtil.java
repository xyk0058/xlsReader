import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;


public class FileUtil {
	
	
	
	//eps为误差值
	private static double eps =1e-6;
	private static int dcmp(double x){if(Math.abs(x) < eps) return 0;else return x<0 ? -1 : 1;}
	
	
	public static void MoveFile(File source, File target) {
	    source.renameTo(target);
	}
	
	public static void CopyFile(File source, File target) {  
	    FileChannel in = null;  
	    FileChannel out = null;  
	    FileInputStream inStream = null;  
	    FileOutputStream outStream = null;  
	    try {  
	        inStream = new FileInputStream(source);  
	        outStream = new FileOutputStream(target);  
	        in = inStream.getChannel();  
	        out = outStream.getChannel();  
	        in.transferTo(0, in.size(), out);
	        inStream.close();
	    	in.close();
	    	outStream.close();
	    	out.close();
	    } catch (IOException e) {  
	        e.printStackTrace();
	    }
	}
	
	public static boolean isAvgMatch(File file, ArrayList<HashMap<String,String>> conditionList){
		String filename = file.getAbsolutePath();
		//System.out.println("filename:"+filename);
		String[] logic = new String[100];
		double[] number = new double[100];
		double[] avg = new double[100];
		int[] columnNum = new int[conditionList.size()+10];
		for(int i=0;i<conditionList.size();i++){
			HashMap<String,String> map = conditionList.get(i);
			columnNum[i] = Integer.parseInt(map.get("columnNum")) - 1;
			logic[i] = map.get("logic");
			number[i] = Double.parseDouble(map.get("number"));
		}
		try {
			InputStreamReader instrr = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(instrr);
			String row = null;
			String[] column = null;
			int cnt = 1;
			row = br.readLine();
			row = br.readLine();
			row = br.readLine();
			row = br.readLine();
			while((row=br.readLine())!=null){
				column = row.split("\\;");
				//System.out.println(cnt+" "+column.length);
				if(column.length == 1){
					break;
				}
				cnt++;
				for(int i=0;i<conditionList.size();i++){
					String record = column[columnNum[i]];
					//System.out.print(record + " ");
					boolean flag = false;
					int dotcnt = 0;
					for(int j=0;j<record.length();j++){
						char c = record.charAt(j);
						if(c <='9' && c >= '0')continue;
						if(c == '.'){
							dotcnt++;
							if(dotcnt <= 1)continue;
						}
						flag = true;
						break;
					}
					if(flag)continue;
					avg[columnNum[i]] += Double.parseDouble(record);
				}
				//System.out.println();
			}
			for(int i=0;i<conditionList.size();i++){
				avg[columnNum[i]] /= cnt;
				String lc = logic[i];
				double num = number[i];
				if(lc.equals("==")){
					if(dcmp(avg[columnNum[i]] - num) != 0){
						return false;
					}
				}else if(lc.equals(">")){
					if(dcmp(avg[columnNum[i]] - num) < 0){
						return false;
					}
				}else if(lc.equals("<")){
					if(dcmp(avg[columnNum[i]] - num) > 0){
						return false;
					}
				}
			}
			br.close();
			instrr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
	public static void NameFileByOrder(String rootPath, ArrayList<Integer> list){
		if(list.size() < 5){
			return ;
		}
		File source = null, target = null;
		String name = null;
		File root = new File(rootPath);
		File[] fileList = root.listFiles();
		for(int i=0;i<fileList.length;i++){
			source = fileList[i];
			name = source.getName();
			if(source.isFile() && name.contains("_")){
				//System.out.print(name + " ");
				System.out.println(name);
				String[] str = name.split("\\_");
				String[] tmp = str[str.length-1].split("\\.");
				str[str.length-1] = tmp[0];
				name = str[list.get(0)-1];
				for(int j=1;j<list.size();j++){
					name += "_"+str[list.get(j)-1];
				}
				name += ".txt";
				//System.out.println(name);
				target = new File(rootPath + "/" + name);
				System.out.println(name);
				//source.renameTo(target);
			}
		}
	}
	
	
	/**
	 * 将目录下所有文件从原始命名格式变为错误码在前的格式
	 * 如：plcb_370201033_20151010_1334_000.txt
	 * 变为 000_plcb_370201033_20151010_1334.txt
	 * FileUtil.NameFileToErrorCodeFirst("F:/Goldwind/山东即墨/山东即墨/山东即墨华能风电场_370201");
	 * @param rootPath
	 */
	public static void NameFileToErrorCodeFirst(String rootPath){
		File source = null, target = null;
		String name = null;
		File root = new File(rootPath);
		File[] fileList = root.listFiles();
		for(int i=0;i<fileList.length;i++){
			source = fileList[i];
			name = source.getName();
			if(source.isFile() && name.contains("_")){
				//System.out.print(name + " ");
				String[] str = name.split("\\_");
				String[] tmp = str[str.length-1].split("\\.");
				name = tmp[0];
				for(int j=0;j<str.length-1;j++){
					name += "_" + str[j];
				}
				name += "." + tmp[1];
				//System.out.println(name);
				target = new File(rootPath + "/" + name);
				source.renameTo(target);
			}
		}
	}
	
	/**
	 * 将目录下所有文件从错误码在前的格式变为原始命名格式
	 * 如：000_plcb_370201033_20151010_1334.txt
	 * 变为 plcb_370201033_20151010_1334_000.txt
	 * FileUtil.NameFileToOrigin("F:/Goldwind/山东即墨/山东即墨/山东即墨华能风电场_370201");
	 * @param rootPath
	 */
	public static void NameFileToOrigin(String rootPath){
		File source = null, target = null;
		String name = null;
		File root = new File(rootPath);
		File[] fileList = root.listFiles();
		for(int i=0;i<fileList.length;i++){
			source = fileList[i];
			name = source.getName();
			if(source.isFile() && name.contains("_")){
				//System.out.print(name + " ");
				String[] str = name.split("\\_");
				String[] tmp = str[str.length-1].split("\\.");
				name = "";
				for(int j=1;j<str.length-1;j++){
					name += str[j] + "_";
					//System.out.print(str[j] + "_");
				}
				name += tmp[0] + "_" + str[0] + "." + tmp[1];
				//System.out.println(name);
				target = new File(rootPath + "/" + name);
				source.renameTo(target);
			}
		}
	}
	
	/**
	 * 根据文件内容分类
	 * 每个HashMap<String,String>对象为一个条件，
	 * 内含三个键值对1.<"columnNum",x> x为列号
	 * 				2.<"logic",y> y为运算符，为"=="，">"，"<"
	 * 				3.<"number",z> z为数值，即具体比较用的值
	 * rootPath是文件夹目录
	 * @param rootPath		文件夹路径
	 * @param conditionList	条件列表
	 */
	public static ArrayList<HashMap<String,String>> SplitFile(String rootPath,
			ArrayList<HashMap<String,String>> conditionList){
		ArrayList<HashMap<String,String>> ret = new ArrayList<HashMap<String,String>>();
		File source = null, target = null;
		String name = null;
		File matchDir = new File(rootPath + "/Matching");	//匹配条件的文件夹
		File misMatchDir = new File(rootPath + "/misMatching");	//不匹配条件的文件夹
		if(!matchDir.exists()){
			matchDir.mkdir();
		}
		if(!misMatchDir.exists()){
			misMatchDir.mkdir();
		}
		File root = new File(rootPath);
		File[] fileList = root.listFiles();
		System.out.println(fileList.length);
		for(int i=0;i<fileList.length;i++){
			source = fileList[i];
			name = source.getName();
			if(source.isFile() && name.contains("_")){
				//System.out.println(i);
				HashMap<String, String> mp = new HashMap<String, String>();
				if(FileUtil.isAvgMatch(source, conditionList)){
					mp.put("filename", name);
					mp.put("isRunNormal", "1");
					ret.add(mp);
					//target = new File(rootPath + "/Matching/" + name);
					//System.out.println("Y " + i + " " + name + source.renameTo(target));
					//CopyFile(source,target);
				} else{
					mp.put("filename", name);
					mp.put("isRunNormal", "0");
					ret.add(mp);
					//target = new File(rootPath + "/misMatching/" + name);
					//System.out.println("N " + i + " " + name + source.renameTo(target));
					//CopyFile(source,target);
				}
				
			}
		}
		return ret;
	}
	
	
	public static void washAllFileUnNumber(String rootpath) {
		File root = new File(rootpath);
		File[] fileList = root.listFiles();
		try{
			for(int i=0;i<fileList.length;i++){
				File file = fileList[i];
				String filename = file.getName();
				if(!file.isFile())continue;
				if(!filename.contains(".txt"))continue;
				washFileUnNumber(rootpath,filename);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void washFileUnNumber(String filepath, String filename){
		try {
			InputStreamReader instrr = new InputStreamReader(new FileInputStream(filepath+filename));
			BufferedReader br = new BufferedReader(instrr);
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(filepath+"/mid.txt"));
			BufferedWriter bw = new BufferedWriter(os);
			String row = null;
			String[] data = null;
			//row = br.readLine();
			while((row=br.readLine())!=null){
				row = row.replace("非数字", "0");
				row = row.replace("null", "0");
				bw.write(row + "\r\n");
			}
			bw.close();
			os.close();
			br.close();
			instrr.close();
			File origin = new File(filepath+filename);
			File newfile = new File(filepath+"/mid.txt");
			if(origin.exists())origin.delete();
			newfile.renameTo(origin);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将rootPath目录下的文件分为bad和good两个文件夹
	 * @param rootPath
	 * @param list
	 */
	public static void Classify(String rootPath,ArrayList<String> list){
		File source = null, target = null;
		String name = null;
		File goodDir = new File(rootPath + "/good");
		File badDir = new File(rootPath + "/bad");
		if(!goodDir.exists()){
			goodDir.mkdir();
		}
		if(!badDir.exists()){
			badDir.mkdir();
		}
		File root = new File(rootPath);
		File[] fileList = root.listFiles();
		for(int i=0;i<fileList.length;i++){
			source = fileList[i];
			name = source.getName();
			//plcb_370201001_20141005_1747_217.txt
			if(source.isFile() && name.contains("_")){
//				String[] str = name.split("\\_");
//				String[] tmp = str[str.length-1].split("\\.");
//				name = tmp[0];
//				for(int j=0;j<str.length-1;j++){
//					name += "_" + str[j];
//				}
//				name += "." + tmp[1];
//				System.out.println(name);
				boolean flag = true;
				for(int j=0;j<list.size();j++){
					String wtid = list.get(j);
					if(name.contains(wtid)){
						flag = false;
						break;
					}
				}
				if(flag){
					target = new File(rootPath + "/good/" + name);
					//System.out.println("good");
					CopyFile(source, target);
				}else{
					target = new File(rootPath + "/bad/" + name);
					//System.out.println("bad");
					CopyFile(source, target);
				}
			}
		}
	}
}
