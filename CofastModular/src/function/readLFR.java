package function;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class readLFR {
	@SuppressWarnings("unchecked")
	public static ArrayList<Object> readFile(String[] strings) throws FileNotFoundException, IOException {
		ArrayList<Object> resultArrayList = new ArrayList<Object>();
		String pathname_cmty = strings[0];
		String pathname_edge = strings[1];
		ArrayList<Integer[]> inite_cmty_data = readfile(pathname_cmty,strings);
		ArrayList<Integer[]> edge_data = readfile(pathname_edge,strings);
		ArrayList<Integer> node_data = new ArrayList<Integer>();
		ArrayList<Object> cmty_data = new ArrayList<Object>();
		ArrayList<Object> belongcmty = new ArrayList<Object>();
		for (int i = 0; i < inite_cmty_data.size(); i++) {
			node_data.add(inite_cmty_data.get(i)[0]);
			belongcmty.add(inite_cmty_data.get(i)[1]);
		}
		
		Set<Object> newbelongSet = new HashSet<>(belongcmty);
		ArrayList<Object> community = new ArrayList<Object>();
		for (int i = 0; i < newbelongSet.size(); i++) {
			ArrayList<Object> subcommunityArrayList = new ArrayList<Object>();
			community.add(subcommunityArrayList);
		}
		for (int i = 0; i < belongcmty.size(); i++) {
			((ArrayList<Object>) community.get((int) belongcmty.get(i))).add(i);
		}
		
		ArrayList<Object> finalArrayList = new ArrayList<Object>();
		finalArrayList.add(node_data);
		finalArrayList.add(edge_data);
		finalArrayList.add(community);
		return finalArrayList;
	}

	private static ArrayList<Integer[]> readfile(String pathname, String[] strings) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		
		try (FileReader reader = new FileReader(pathname);
				 BufferedReader br = new BufferedReader(reader)){

			String line;
			// community file 
			if (pathname == strings[0]) {		
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				while ((line = br.readLine())!=null) {					
					String[] source = line.replace(" ", "").split("\t");
					Integer[] aa = new Integer[source.length];
					for(int i=0;i< source.length;i++) {
						aa[i] = Integer.valueOf(source[i]).intValue()-1;
					}
					list.add(aa);									
				}		
			}
			// edge file 
			else {
				while ((line = br.readLine())!=null) {
					String[] source = line.split("\t");	
					Integer[] aa = new Integer[source.length];
					for(int i=0;i< source.length;i++) {
						aa[i] = Integer.valueOf(source[i]).intValue()-1;
					}
					list.add(aa);				
				}			
			}
								
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return list;
	}
	
}
