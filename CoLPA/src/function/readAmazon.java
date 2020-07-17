package function;


import java.io.BufferedReader;

import java.io.FileReader;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class readAmazon {

	public static ArrayList<Object> readFile(String[] strings) {	
		
		
		String pathname_cmty = strings[0];
		String pathname_edge = strings[1];
		//Read and compute the information of the initial network
		
		//node_data, edge_data. cmty_data, belongCmty, degree
		
		ArrayList<Integer[]> cmty_data = readfile(pathname_cmty,strings);
		ArrayList<Integer[]> all_edge_data = readfile(pathname_edge,strings);
		
		ArrayList<Integer> node_data = new ArrayList<Integer>(); 
		ArrayList<Integer[]> edge_data = new ArrayList<Integer[]>();

				
		for(int i=0;i<cmty_data.size();i++) {
			for(int j=0;j<cmty_data.get(i).length;j++) {				
				if(!node_data.contains(cmty_data.get(i)[j])) {
					node_data.add(cmty_data.get(i)[j]);
				}	
			}
		}									
	
		Collections.sort(node_data, new Comparator<Object>() {
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

		for(int i=0;i<all_edge_data.size();i++) {
			if((node_data.contains(all_edge_data.get(i)[0])) && (node_data.contains(all_edge_data.get(i)[1]))) {
				edge_data.add(all_edge_data.get(i));		
			}
		}		
		// Replace node and edge label
		// Build a dictionary of the source node and its index
		Map<Object, Object> dictMap = new HashMap<>();
		for (int i = 0; i < node_data.size(); i++) {
			dictMap.put(node_data.get(i), i);
		}
		// Replace node label
		ArrayList<Object> newNode_data = new ArrayList<Object>();
		for (int i = 0; i < node_data.size(); i++) {
			newNode_data.add(dictMap.get(node_data.get(i)));
		}
		// Replace index of edge
		ArrayList<Integer[]> newEdge_data = new ArrayList<Integer[]>(); 
		for (int i = 0; i < edge_data.size(); i++) {
			Integer[] edgeIntegers = new Integer[2];
			edgeIntegers[0] = (Integer) dictMap.get(edge_data.get(i)[0]);
			edgeIntegers[1] = (Integer) dictMap.get(edge_data.get(i)[1]);
			newEdge_data.add(edgeIntegers);
		}
		ArrayList<Object> cmArrayList = new ArrayList<Object>();	
		ArrayList<Object> resultArrayList = new ArrayList<Object>();
		resultArrayList.add(newNode_data);
		resultArrayList.add(newEdge_data);
		resultArrayList.add(cmArrayList);
		return resultArrayList;
	}
	
	// Read community file and edge file
	public static ArrayList<Integer[]> readfile(String pathname,String[] string) {
		
		
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
						
		try (FileReader reader = new FileReader(pathname);
			 BufferedReader br = new BufferedReader(reader)){

			String line;
			// community file 
			if (pathname == string[0]) {				
				while ((line = br.readLine())!=null) {
					String[] source = line.split("\t");	
					Integer[] aa = new Integer[source.length];
					for(int i=0;i< source.length;i++) {
						aa[i] = Integer.valueOf(source[i]).intValue();
					}
					list.add(aa);									
				}				
			}
			// edge file 
			else {

				for (int i=0;i<4;i++) {
					br.readLine();
				}
				while ((line = br.readLine())!=null) {
					String[] source = line.split("\t");	
					Integer[] aa = new Integer[source.length];
					for(int i=0;i< source.length;i++) {
						aa[i] = Integer.valueOf(source[i]).intValue();
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



