package function;

import java.awt.Label;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class readFile4 {
	@SuppressWarnings("unchecked")
	public static ArrayList<Object> readfile(String pathname) throws FileNotFoundException, IOException {	
	
	ArrayList<Object> node_data = new ArrayList<Object>();
	ArrayList<Object> edge_data = new ArrayList<Object>();
	ArrayList<Object> belongcmty = new ArrayList<Object>();
	ArrayList<Object> finalreArrayList = new ArrayList<Object>();	
	
	
	try (FileReader reader = new FileReader(pathname);
			 BufferedReader br = new BufferedReader(reader)){
		//Read nodes
		br.readLine();
		while(br.readLine().contains("node")) {				
			node_data.add(Integer.valueOf(br.readLine().split(" ")[5]).intValue());
			br.readLine();
			br.readLine();
		}
		//Read edge	
		Integer[] aa = new Integer[2];			
		aa[0] = Integer.valueOf(br.readLine().split(" ")[5]).intValue();
		aa[1] = Integer.valueOf(br.readLine().split(" ")[5]).intValue();
		edge_data.add(aa);	
		while(br.readLine()!=null) {
			if(br.readLine().equals("]")) {
				break;
			}
			Integer[] aa1 = new Integer[2];
			aa1[0] = Integer.valueOf(br.readLine().split(" ")[5]).intValue();
			aa1[1] = Integer.valueOf(br.readLine().split(" ")[5]).intValue();
			edge_data.add(aa1);
		}		
	}
	int count = node_data.size();
	String pathnameString = "./sourceDate/strike.txt";
	try (FileReader reader = new FileReader(pathnameString);
			 BufferedReader br = new BufferedReader(reader)){
		for (int i = 0; i < count; i++) {
			belongcmty.add(Integer.valueOf(br.readLine()).intValue());
		}	
	}
	Set<Object> newbelongSet = new HashSet<>(belongcmty);
	ArrayList<Object> communityArrayList = new ArrayList<Object>();
	for (int i = 0; i < newbelongSet.size(); i++) {
		ArrayList<Object> subcommunityArrayList = new ArrayList<Object>();
		communityArrayList.add(subcommunityArrayList);
	}
	for (int i = 0; i < belongcmty.size(); i++) {
		((ArrayList<Object>) communityArrayList.get((int) belongcmty.get(i)-1)).add(i);
	}
	finalreArrayList.add(node_data);
	finalreArrayList.add(edge_data);
	Collections.sort((ArrayList<Object>) communityArrayList, new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			if ((int)((ArrayList<Object>) o1).get(0) > (int)((ArrayList<Object>) o2).get(0)) {
				return 1;
			}
			else {
				return -1;
			}
		}
	});
	finalreArrayList.add(communityArrayList);
	return finalreArrayList;
	}
}
