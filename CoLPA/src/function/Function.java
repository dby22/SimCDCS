package function;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Function {
	
	
	
	// construct send data
	public static String conSendInfor2(int[] N) {
		String aaString = "";
		for (int i = 0; i < N.length; i++) {
			aaString = aaString + Integer.toString(N[i]) + " ";
		}
		return aaString;
	}
	
	public static String conSendInfor1(int[][][] infor) {
		String aaString = "";
		for (int i = 0; i < infor.length; i++) {
			for (int j = 0; j < infor[i].length; j++) {
				for (int j2 = 0; j2 < infor[i][j].length; j2++) {
					aaString = aaString + Integer.toString(infor[i][j][j2]) + " ";
				}
			}
		}
		return aaString;
	}
	
	public static double[] conNewDelta(double[][] deltaQ2) {
		double[] newDelta = new double[deltaQ2.length*(deltaQ2.length-1)/2+1];
		newDelta[0] = 0;
		int k = 1;
		for (int i = 0; i < deltaQ2.length-1; i++) {
			for (int j = i+1; j < deltaQ2.length; j++) {
				newDelta[k++] = deltaQ2[i][j];
			}
		}
		return newDelta;
	}
	
	// convert index
	public static int[] convert(int k, int length) {
		int sum = 0;
		int[] index = new int[2]; // row and col
		for (int i = length-1; i > -1; i--) {
			if (sum + i >= k) {
				index[0] = length-i-1;
				index[1] = k-sum+length-i-1;
				break;
			}
			else {
				sum = sum + i;
			}
		}
		return index;
	}
	
	// convert to matrix
	public static int[] conToMatrix(String reciveString, int k){
		String[] dta = reciveString.split(" ");
		int[] N = new int[k];				
		for (int i = 0; i < dta.length; i++) {
			N[i] = Integer.valueOf(dta[i]);	
		}		
		return N;
	}
	
	public static int[][][] conToMatrix3(String reciveString, int d, int k){
		String[] dta = reciveString.split(" ");
		int[][][] K = new int[d][2][k];		
		int[] tmp = new int[2*k];
		tmp[0] = Integer.valueOf(dta[0]);
		int row = 0;
		for (int i = 1; i < dta.length; i++) {
			if (i%(2*k)==0 ) {
				for (int j = 0; j < k; j++) {
					K[row][0][j] = tmp[j];
				}
				for (int j = k; j < 2*k; j++) {
					K[row][1][j-k] = tmp[j];
				}
				row = row + 1;
			}
			tmp[i%(2*k)] = Integer.valueOf(dta[i]);			
		}
		for (int j = 0; j < k; j++) {
			K[row][0][j] = tmp[j];
		}
		for (int j = k; j < 2*k; j++) {
			K[row][1][j-k] = tmp[j];
		}		
		return K;
	}
	
	
	
	
	// Initialization a_i(sum of degree of all the nodes in the community i)
	public static double[] initeDegree(int N, int m, ArrayList<Integer[]> edge_data, ArrayList<Object> node_data){
		double[] a_i= new double[N];		
		for(int i=0;i<node_data.size();i++) {
			for (int j = 0; j < edge_data.size(); j++) {
				if (edge_data.get(j)[0].equals(node_data.get(i))  ||
						edge_data.get(j)[1].equals(node_data.get(i)) ) {
					a_i[i]++;
				}
			}			
		}
		for (int i = 0; i < a_i.length; i++) {
			a_i[i] = a_i[i]/(2*m);
		}
		return a_i;
	}
	
	// Initialization deltaQ
	public static double[][] initeDeltaQ(int N, int m, double[] a_i, ArrayList<Integer[]> edge_data) {
		double[][] delta_Q = new double[N][N];
		int flag = 0;
		for (int i = 0; i < delta_Q.length; i++) {
			for (int j = 0; j < delta_Q[i].length; j++) {
				if(i==j) {
					delta_Q[i][j] = 0;
				}
				else{
					for (int k = 0; k < edge_data.size(); k++) {
						if ((edge_data.get(k)[0].equals(i) && edge_data.get(k)[1].equals(j)) || 
								(edge_data.get(k)[0].equals(j) && edge_data.get(k)[1].equals(i))) {
							delta_Q[i][j] = (double)1/(double)(m) - (double)(2*a_i[i]*a_i[j]);
							delta_Q[j][i] = (double)1/(double)(m) - (double)(2*a_i[i]*a_i[j]);
							flag = 1;
							break;
						}
					}
					if (flag == 0) {
						delta_Q[i][j] = 0;
						delta_Q[j][i] = 0;
					}
				}				
			}
		}
		return delta_Q;
	}
		
	// update the information (deltaQ,ai.community)
	@SuppressWarnings("unchecked")
	public static void update(double[][] deltaQ, double[] a_i, ArrayList<Object> community, int max, int min, ArrayList<Integer[]> edge_data) {
		
		// update deltaQ
		for (int i = 0; i < community.size(); i++) {
			ArrayList<Integer[]> smallEdgeArrayList = new ArrayList<Integer[]>();
			ArrayList<Integer[]> lagerEdgeArrayList = new ArrayList<Integer[]>();			
			if (community.get(i) instanceof ArrayList && i!=min && i!=max) {				
				// looking for the edges that may connect to the small and large community
				for (int j = 0; j < ((ArrayList<Object>) community.get(i)).size(); j++) {
					// the edges that may connect to the small community in community i
					for (int j2 = 0; j2 < ((ArrayList<Object>) community.get(min)).size(); j2++) {
						Integer[] tempIntegers1 = {(Integer) ((ArrayList<Object>) community.get(i)).get(j),(Integer) ((ArrayList<Object>) community.get(min)).get(j2)};
						Integer[] tempIntegers2 = {(Integer) ((ArrayList<Object>) community.get(min)).get(j2),(Integer) ((ArrayList<Object>) community.get(i)).get(j)};
						smallEdgeArrayList.add(tempIntegers1);
						smallEdgeArrayList.add(tempIntegers2);
					}
					// the edges that may connect to the large community in community i
					for (int j2 = 0; j2 < ((ArrayList<Object>) community.get(max)).size(); j2++) {
						Integer[] tempIntegers3 = {(Integer) ((ArrayList<Object>) community.get(i)).get(j),(Integer) ((ArrayList<Object>) community.get(max)).get(j2)};
						Integer[] tempIntegers4 = {(Integer) ((ArrayList<Object>) community.get(max)).get(j2),(Integer) ((ArrayList<Object>) community.get(i)).get(j)};
						lagerEdgeArrayList.add(tempIntegers3);
						lagerEdgeArrayList.add(tempIntegers4);
					}
				}	
							
				int flag1 = 0;
				int flag2 = 0;
				for (int j = 0; j < smallEdgeArrayList.size(); j++) {
					for (int j2 = 0; j2 < edge_data.size(); j2++) {
						Integer[] aaIntegers = smallEdgeArrayList.get(j);
						Integer[] bbIntegers = edge_data.get(j2);
						if ((aaIntegers[0].equals(bbIntegers[0]) && aaIntegers[1].equals(bbIntegers[1])) || 
								(aaIntegers[0].equals(bbIntegers[1]) && aaIntegers[1].equals(bbIntegers[0]))) {
							flag1 = 1;
							break;
						}
					}	
					if (flag1 == 1) {
						break;
					}
				}
				for (int j2 = 0; j2 < lagerEdgeArrayList.size(); j2++) {
					for (int j = 0; j < edge_data.size(); j++) {
						Integer[] aaIntegers = lagerEdgeArrayList.get(j2);
						Integer[] bbIntegers = edge_data.get(j);
						if ((aaIntegers[0].equals(bbIntegers[0]) && aaIntegers[1].equals(bbIntegers[1])) || 
								(aaIntegers[0].equals(bbIntegers[1]) && aaIntegers[1].equals(bbIntegers[0]))) {
							flag2 = 1;
							break;
						}
					}
					if (flag2 == 1) {
						break;
					}
				}
														
				// only connect to the small community
				if (flag1 == 1 && flag2 == 0) {
					deltaQ[i][min] = deltaQ[i][min] - 2*a_i[max]*a_i[i]; 
					deltaQ[min][i] = deltaQ[i][min];
				}
				// only connect to the large community
				if (flag1 == 0 && flag2 == 1) {
					deltaQ[i][min] = deltaQ[i][max] - 2*a_i[min]*a_i[i];
					deltaQ[min][i] = deltaQ[i][min];
				}
				// both connect to the small and large communities
				if (flag1 == 1 && flag2 == 1){
					deltaQ[i][min] = deltaQ[i][min] + deltaQ[i][max];
					deltaQ[min][i] = deltaQ[i][min];
				}
			}
		}
		// delete the large community (set the location in the matrix to -1)
		for (int i1 = 0; i1 < deltaQ.length; i1++) {
			if (i1 == max) {
				for (int j = 0; j < deltaQ[i1].length; j++) {
					deltaQ[i1][j] =-1;
				}
			}
			else {
				deltaQ[i1][max] =-1;
			}			
		}
		// merging community
		for (int i1 = 0; i1 < ((ArrayList<Object>) community.get(max)).size(); i1++) {
			((ArrayList<Object>) community.get(min)).add(((ArrayList<Object>) community.get(max)).get(i1));
		}
		community.set((int)max,-1);
		// update a_i
		a_i[min] = a_i[min] + a_i[max];
		a_i[max] = (Integer) (-1);
	}
		
	// Initial two graph
	@SuppressWarnings("unchecked")
	public static ArrayList<Object> conGragh(ArrayList<Object> data, double p1, double p2, int seed){
		
		ArrayList<Object> node_data = (ArrayList<Object>) data.get(0);
 		ArrayList<Integer[]> edge_data = (ArrayList<Integer[]>) data.get(1);
		ArrayList<Object> graghEdge1 = new ArrayList<Object>();
		ArrayList<Object> graghEdge2 = new ArrayList<Object>();
		ArrayList<Object> graghNode1 = new ArrayList<Object>();
		ArrayList<Object> graghNode2 = new ArrayList<Object>();
		ArrayList<Object> graghArrayList1 = new ArrayList<Object>();
		ArrayList<Object> graghArrayList2 = new ArrayList<Object>();
		ArrayList<Object> graghArrayList = new ArrayList<Object>();
		Random r1 = new Random();		
		r1.setSeed(seed);
		
		// Assign edges to two networks, each of which must exist in at least one network
		for (int i = 0; i < edge_data.size(); i++) {
			double rand1 = r1.nextDouble();
			if (rand1<=p1+p2-1) {
				graghEdge1.add(edge_data.get(i));
				graghEdge2.add(edge_data.get(i));
			}
			else if (rand1 > p1+p2-1 && rand1 <= p1) {
				graghEdge1.add(edge_data.get(i));
			}
			else {
				graghEdge2.add(edge_data.get(i));
			}
		}
		// Determine the nodes of the network
		for (int i = 0; i < node_data.size(); i++) {
			graghNode1.add(node_data.get(i));
			graghNode2.add(node_data.get(i));
		}
		Collections.sort((List) graghNode1);
		Collections.sort((List) graghNode2);
		graghArrayList1.add(graghNode1);
		graghArrayList1.add(graghEdge1);
		graghArrayList2.add(graghNode2);
		graghArrayList2.add(graghEdge2);
		graghArrayList.add(graghArrayList1);
		graghArrayList.add(graghArrayList2);
		return graghArrayList;
	}
			
	// read graph files 
	public static ArrayList<Object> readSelf(int index) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		Map<Object, Object> dictMap = new HashMap<>();
		
		String[] strings = {"football","karate","dolphins","strike",
				"amazon","CA-GrQc","WiKi-Vote","email-Eu-core","facebook_combined",
				"LFR_1000","LFR_3000","LFR_5000","LFR_7000","LFR_9000"};
		
		for (int i = 0; i < strings.length; i++) {
			dictMap.put(i, strings[i]);
		}
		
		String pathname = "./sourceDate/";
		String stringname = (String) dictMap.get(index);
		ArrayList<Object> dateArrayList = new ArrayList<Object>();
		
		if (index>=0 && index<=3) {			
			// read small-scale networks
			pathname = pathname + stringname + ".gml";	
			if (index==0) {
				dateArrayList = readFile.readfile(pathname);
			}
			else if (index==1) {
				dateArrayList = readFile2.readfile(pathname);
			}	 
			else if (index==2) {
				dateArrayList = readFile3.readFile(pathname);
			}
			else if (index==3) {
				dateArrayList = readFile4.readfile(pathname);
			}				
		}	
		else if (index>=4 && index<=8) {
			// read small-scale networks		
			if (index==4) {
				String[] newPathname = new String[2];
				newPathname[0] = pathname + "\\largeData\\" + stringname + ".cmty.txt";
				newPathname[1] = pathname + "\\largeData\\" + stringname + ".ungraph.txt";
				dateArrayList = readAmazon.readFile(newPathname);				
			}
			else if (index==5 || index==6) {
				pathname = pathname + "\\largeData\\"+stringname + ".txt";
				dateArrayList = readFile56.readfile(pathname);
			}
			else {
				pathname = pathname + "\\largeData\\"+stringname + ".txt";
				dateArrayList = readFile78.readfile(pathname);
			}
		}
		else {
			// read networks generated by LFR
			String[] newPathname = new String[2];
			newPathname[0] = pathname + "\\largeData\\" + stringname + "\\community.dat";
			newPathname[1] = pathname + "\\largeData\\" + stringname + "\\network.dat";
			dateArrayList = readLFR.readFile(newPathname);
		}
		return dateArrayList;
	}	
	
	// get the information(deltaQ,a_i,community,edge_data)
    @SuppressWarnings("unchecked")
	public static ArrayList<Object> getInitV(int flag) throws FileNotFoundException, IOException {
    	
    	ArrayList<Object> temp = Function.conGragh(Function.readSelf(3), 0.6, 0.8, 1);  
    	ArrayList<Object> graph = new ArrayList<Object>();
    	if (flag == 1) {
    		graph = (ArrayList<Object>) temp.get(0);
		}
    	else {
    		graph = (ArrayList<Object>) temp.get(1);
		}   
    	ArrayList<Object> node_data = (ArrayList<Object>) graph.get(0);
    	ArrayList<Integer[]> edge_data = (ArrayList<Integer[]>) graph.get(1);	
    	int m = edge_data.size();
    	int N = node_data.size();
    	double[] a_i = Function.initeDegree(N, m, edge_data, node_data);  
    	double[][] deltaQ = Function.initeDeltaQ(N, m, a_i, edge_data);
    	ArrayList<Object> community = new ArrayList<Object>();
    	for (int i = 0; i < N; i++) {
    		ArrayList<Object> subcommunity = new ArrayList<Object>();
    		subcommunity.add(node_data.get(i));
    		community.add(subcommunity);
    	}
    	ArrayList<Object> detaiList = new ArrayList<Object>();
    	detaiList.add(deltaQ);
    	detaiList.add(a_i);
    	detaiList.add(community);
    	detaiList.add(edge_data);    	
    	return detaiList;
	}
    
    // looking for the neighbors of all the nodes
 	@SuppressWarnings("unchecked")
 	public static ArrayList<Object> lookNeighber(ArrayList<Object> gragh, ArrayList<Object> node_data) {
 				
 		ArrayList<Integer[]> edge_data = (ArrayList<Integer[]>) gragh.get(1);		
 		ArrayList<Object> neighber = new ArrayList<Object>();
 		for (int i = 0; i < node_data.size(); i++) {
 			ArrayList<Object> subNeighber = new ArrayList<Object>();
 			neighber.add(subNeighber);
 		}			
 		for (int i = 0; i < node_data.size(); i++) {
 			for (int j = 0; j < edge_data.size(); j++) {
 				if (edge_data.get(j)[0].equals(node_data.get(i))) {
 					((ArrayList<Object>) neighber.get(i)).add(edge_data.get(j)[1]);
 				}
 				if (edge_data.get(j)[1].equals(node_data.get(i))) {
 					((ArrayList<Object>) neighber.get(i)).add(edge_data.get(j)[0]);
 				}
 			}
 		}
 		return neighber;
 	}

    
    @SuppressWarnings("unchecked")
	public static ArrayList<Object> getIniteL(int flag) throws FileNotFoundException, IOException {
    	ArrayList<Object> temp = Function.conGragh(Function.readSelf(3), 0.6, 0.8, 1);  
    	ArrayList<Object> graph = new ArrayList<Object>();
    	if (flag == 1) {
    		graph = (ArrayList<Object>) temp.get(0);
    	}
    	else {
    		graph = (ArrayList<Object>) temp.get(1);
		} 
    	ArrayList<Object> node_data = (ArrayList<Object>) graph.get(0);
		ArrayList<Object> neighber = lookNeighber(graph, node_data);
		ArrayList<Integer> lableList = new ArrayList<Integer>();
		for (int i = 0; i < node_data.size(); i++) {
			lableList.add(i);
		}	
		ArrayList<Object> finalArrayList = new ArrayList<Object>();
		finalArrayList.add(neighber);
		finalArrayList.add(node_data);
		finalArrayList.add(lableList);
		return finalArrayList;
	}
    
    public static boolean judgeLabel(ArrayList<Object> oldlabeList, ArrayList<Object> newlabeList) {
    	int flag = 0;
    	for (int i = 0; i < oldlabeList.size(); i++) {
			if (!oldlabeList.get(i).equals(newlabeList.get(i))) {
				flag = 1;
				return false;
			}
		}
    	if (flag == 0) {
			return true;
		}
    	else {
			return false;
		}
    }
    
    public static void update(ArrayList<Object> oldlabeList, ArrayList<Object> newlabeList) {
    	for (int i = 0; i < oldlabeList.size(); i++) {
			oldlabeList.set(i, (Integer) newlabeList.get(i));
		}
    }
    
    
	@SuppressWarnings("unchecked")
	public static ArrayList<Object> comWeigh(ArrayList<Object> node_data, ArrayList<Object> neighber, ArrayList<Object> lableList) {
    	
    	ArrayList<Object> alllabelWeighted = new ArrayList<Object>();
		for (int i = 0; i < node_data.size(); i++) {
			int[] lableWeighted = new int[node_data.size()];
			for (int j = 0; j < node_data.size(); j++) {
				lableWeighted[j] = 0;		
			}
			ArrayList<Object> node_neighber = (ArrayList<Object>) neighber.get(i);	
			// the set of neighbor label list of network 1
			if (node_neighber.size()>0) {
				for (int j = 0; j < node_neighber.size(); j++) {
					int index = (int) lableList.get((int) node_neighber.get(j));				
					lableWeighted[index] ++ ;
				}
			}	
			alllabelWeighted.add(lableWeighted);
		}	
		return alllabelWeighted;
    }
}
