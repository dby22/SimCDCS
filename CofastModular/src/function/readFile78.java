package function;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class readFile78 {
public static ArrayList<Object> readfile(String pathname) throws FileNotFoundException, IOException {
		
		ArrayList<Object> node_data = new ArrayList<Object>();
		ArrayList<Integer[]> edge_data = new ArrayList<Integer[]>();
		
		try (FileReader reader = new FileReader(pathname);
				 BufferedReader br = new BufferedReader(reader)){

			// Read edge
			ArrayList<Integer[]> list = new ArrayList<Integer[]>();
			String line;
			while ((line = br.readLine())!=null) {
				String[] source = line.split(" ");	
				Integer[] aa = new Integer[source.length];
				for(int i=0;i< source.length;i++) {
					aa[i] = Integer.valueOf(source[i]).intValue();
				}
				list.add(aa);				
			}	

			// Find nodes based on edge
			ArrayList<Object> tempNode_data = new ArrayList<Object>();
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).length; j++) {
					if (!tempNode_data.contains(list.get(i)[0])) {
						tempNode_data.add(list.get(i)[0]);
					}
					if (!tempNode_data.contains(list.get(i)[1])) {
						tempNode_data.add(list.get(i)[1]);
					}
				}
			}			
			Collections.sort(tempNode_data,new Comparator<Object>() {

				@Override
				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					if ((int)o1 > (int)o2) {
						return 1;
					}
					else {
						return -1;
					}
				}
			});						
			// Building Dictionary of node index and number
			Map<Object, Object> dictMap = new HashMap<>();
			for (int i = 0; i < tempNode_data.size(); i++) {
				dictMap.put(tempNode_data.get(i),i);
			}
			// Transformation node
			for (int i = 0; i < tempNode_data.size(); i++) {
				node_data.add(dictMap.get(tempNode_data.get(i)));
			}
			// Conversion edge
			for (int i = 0; i < list.size(); i++) {
				Integer[] edge = new Integer[2];
				edge[0] = (Integer) dictMap.get(list.get(i)[0]);
				edge[1] = (Integer) dictMap.get(list.get(i)[1]);
				edge_data.add(edge);
			}
		}
		ArrayList<Object> cmtyArrayList = new ArrayList<Object>();
		ArrayList<Object> finalArrayList = new ArrayList<Object>();
		finalArrayList.add(node_data);
		finalArrayList.add(edge_data);
		finalArrayList.add(cmtyArrayList);
		return finalArrayList;		
	}
}
