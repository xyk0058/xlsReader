import java.awt.List;
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
import java.util.ArrayList;
import java.util.HashMap;



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String root = "/Volumes/移动硬盘/数据/bin文件数据/黑色粉末B文件数据/";
		
		//FileUtil.washFileUnNumber("/Users/xyk0058/Documents/GOLDWIND/sdjm/sdjm/sdjm_370201/bad/misMatching/","plcb_370201006_20140912_1308_227.txt");
		//FileUtil.washAllFileUnNumber("/Users/xyk0058/Documents/GOLDWIND/sdjm/sdjm/sdjm_370201/bad/misMatching/");
//		ArrayList<Integer> list = new ArrayList<Integer>();
//		list.add(5);
//		list.add(4);
//		list.add(3);
//		list.add(2);
//		list.add(1);
//		FileUtil.NameFileByOrder("F:/Goldwind/山东即墨/山东即墨/山东即墨华能风电场_370201/", list);
		ArrayList<String> list = null;
		try {
			list = XlsUtil.read("/Users/xyk0058/Documents/GOLDWIND/黑色粉末数据列表（2015-12-09 更新） - 副本.xlsx");
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i));
			}
			//FileUtil.Classify("/Users/xyk0058/Documents/GOLDWIND/山东即墨/山东即墨/山东即墨华能风电场_370201/",list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		FileUtil.washAllFileUnNumber("/Users/xyk0058/Documents/GOLDWIND/sdjm/sdjm/sdjm_370201/bad/");
//		FileUtil.washAllFileUnNumber("/Users/xyk0058/Documents/GOLDWIND/sdjm/sdjm/sdjm_370201/good/");
		
		ArrayList<HashMap<String, String>> conditionList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map = new HashMap<String, String>();
		map.put("columnNum", "20");
		map.put("logic", ">");
		map.put("number", "8.5");
		conditionList.add(map);
		
		ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
		File rootdir = new File(root);
		File[] childDir = rootdir.listFiles();
		for(int i=0;i<childDir.length;i++){
			File dir = childDir[i];
			res.clear();
			System.out.println("/Volumes/移动硬盘/数据/bin文件数据/黑色粉末B文件数据/"+dir.getName()+"/");
			ArrayList<HashMap<String, String>> ret = FileUtil.SplitFile("/Volumes/移动硬盘/数据/bin文件数据/黑色粉末B文件数据/"+dir.getName()+"/",conditionList);
			for(int j=0;j<ret.size();j++){
				HashMap<String, String> tmp = ret.get(j);
				String name = tmp.get("filename");
				boolean flag = false;
				for(int k=0;k<list.size();k++){
					if(name.contains(list.get(k))){
						flag = true;
						break;
					}
				}
				if(flag){
					tmp.put("isGood", "1");
				}else{
					tmp.put("isGood", "0");
				}
				res.add(tmp);
			}
			writeList(dir.getName(),res);
		}
		
		
//		File file = new File("F:/Goldwind/山东即墨/山东即墨/山东即墨华能风电场_370201/plcb_370201001_20140803_0514_000.txt");
//		if(FileUtil.isAvgMatch(file, list)){
//			System.out.println("Yes");
//		} else{
//			System.out.println("No");
//		}
	}
	
	public static void writeList(String rootPath, ArrayList<HashMap<String, String>> list) {
		File file = new File("/Users/xyk0058/Desktop/BIN/");
		if(!file.exists())file.mkdirs();
		try {
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream("/Users/xyk0058/Desktop/BIN/"+rootPath+".txt"));
			BufferedWriter bw = new BufferedWriter(os);
			for(int i=0;i<list.size();i++){
				HashMap<String, String> mp = list.get(i);
				bw.write(mp.get("filename") + " " + mp.get("isRunNormal") + " " + mp.get("isGood") + "\r\n");
			}
			bw.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
