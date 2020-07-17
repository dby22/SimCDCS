package function;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class OT {
	
	public static void main(String[] args) {
		double a = -1.234;
		double b = -1.2345;
		System.out.println(Bconstrut(b, (int[][][])Aconstruct(a).get(0), (int[])Aconstruct(a).get(1), (int)Aconstruct(a).get(2)));
	}

	// Alice construct the K and S
	public static ArrayList<Object> Aconstruct(double num) {
		int d = 32;
		int k = 128;
		int u = 128; //(0£¬2k) rol
		int v = 128; //[0,k]
		// Alice 
		int flag = 0;
		if (num<0) {
			flag = 1;
		}
		String string = convert(num, d, flag);		
		int[][][] K = conK(string, k, v);
		int[][] S = conS(string, K, k);		
		// k and S XOR and pass to BOb
		SXor(K, S, u);
		// S XOR and pass to Bob
		int[] N = Xor(S, u);	
		ArrayList<Object> finalArrayList = new ArrayList<Object>();
		finalArrayList.add(K);
		finalArrayList.add(N);
		finalArrayList.add(flag);
		return finalArrayList;	
	}
	
	// Bob 
	public static String Bconstrut(double numb, int[][][] K, int[] N, int flag) {
		int d = 32;
		int k = 128;
		int u = 128; //(0£¬2k) ÒÆÎ»
		int flagB = 0;
		if (numb < 0) {
			flagB = 1;
		}		
		if ((flag^flagB) == 1) {
			if (flag==1) {
				return "B";
			}
			else {
				return "A";
			}
		}
		else {
			String stringb = convert(numb, d, flagB);
			// OT
			int[][] ot = myOT(K, d, k, stringb);
			int[] newot = Xor(ot, u);
			int[] result = fin(N, newot);
			for (int i = 0; i < result.length-1; i++) {
				if (result[i]==1) {
					if (result[i+1] == 1) {
						if (flag == 1) {
							return "B";
						}
						else {
							return "A";
						}					
					}
					else if (result[i+1] == 0){
						if (flag == 1) {
							return "A";
						}
						else {
							return "B";
						}						
					}
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				return "B";
			}
			return stringb;
		}
	}
	
	// Get the final string 
	public static int[] fin(int[] a, int[] b) {
		int[] result = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i]^b[i];
		}
		return result;
	}
	
	// OT
	public static int[][] myOT(int[][][] K, int d, int k, String string) {
		int[][] ot = new int[d][k];
		for (int i = 0; i < string.toCharArray().length; i++) {
			if (string.toCharArray()[i]=='0') {
				System.arraycopy(K[i][0], 0, ot[i], 0, k);
			}
			else {
				System.arraycopy(K[i][1], 0, ot[i], 0, k);
			}
		}
		return ot;
	}

	// Binary representation of floating-point numbers
	public static String convert(double num, int length, int flag) {
		
		if (flag == 1) {
			num = -num;
		}
		int Inte_part = (int)num;
		double Frac_part = num%1;
		ArrayList<String> finaList = new ArrayList<String>();
		
		while (true) {
			if (Inte_part/2!=0) {
				finaList.add(Integer.toString(Inte_part%2));
				Inte_part = Inte_part/2;
			}
			else {
				finaList.add(Integer.toString(Inte_part));
				break;
			}
		}
		while (finaList.size()<(int)length/2) {
			finaList.add("0");
		}
		Collections.reverse(finaList);		
		while (Frac_part>0) {
			if (Frac_part*2>=1) {
				finaList.add("1");
				Frac_part = Frac_part*2-1;
			}
			else {
				finaList.add("0");
				Frac_part = Frac_part*2;
			}
			if (finaList.size() == length) {
				break;
			}
		}		
		String binayString = String.join("", finaList);
		while (binayString.length()<length) {
			binayString = binayString+"0";
		}
		return binayString;
	}

	// Construct matrix K
	public static int[][][] conK(String string, int k, int v) {
		int[][][] K = new int[string.toCharArray().length][2][k];		
		for (int i = 0; i < K.length; i++) {
			// set random 
			for (int j = 0; j < 2; j++) {
				for (int j2 = v; j2 < k; j2++) {
					K[i][j][j2] = 0;
				}
			}		
			for (int j = 0; j < 2*i; j++) {
				if(string.toCharArray()[i]==0) {
					K[i][1][j] = 0;
				}
				else if (string.toCharArray()[i]==1){
					K[i][0][j] = 0;
				}
			}
			// set the value
			if (string.toCharArray()[i]=='0') {
				K[i][1][2*i+1] = 0;
				K[i][1][2*i] = 1;
			}
			else if (string.toCharArray()[i]=='1'){
				K[i][0][2*i+1] = 1;
				K[i][0][2*i] = 1;
			}
		}
		return K;
	}

	// Construct matrix S
	public static int[][] conS(String string, int[][][] K, int k) {
		int [][] s = new int[string.toCharArray().length][k];
		for (int i = 0; i < string.toCharArray().length; i++) {
			for (int j = 0; j < string.toCharArray().length; j++) {
				s[i][j] = 0;
			}
		}
		s[string.toCharArray().length-1][k-2] = 1^bXor(s, k-2)^tXor(K, 0, k-2);
		s[string.toCharArray().length-1][k-1] = 1^bXor(s, k-1)^tXor(K, 0, k-1);
		return s;
	}
	
	// bXOR and return value
	public static int bXor(int[][] arr, int index) {
		int tmp = arr[0][index];
		for (int i = 1; i < arr.length; i++) {
			tmp = tmp^arr[i][index];
		}
		return tmp;
	}
	
	// tXOR and return value
	public static int tXor(int[][][] arr, int index1, int index2) {
		int tmp = arr[0][index1][index2];
		for (int i = 1; i < arr.length; i++) {
			tmp = tmp^arr[i][index1][index2];
		}
		return tmp;
	}
	
	// XOR from one array and return array
	public static int[] Xor(int[][] arr, int u){
		int[] tmp = new int[arr[0].length];
		for (int i = 0; i < arr[0].length; i++) {
			tmp[i] = arr[0][i];
			for (int j = 0; j < arr.length; j++) {
				tmp[i] = tmp[i]^arr[j][i];
			}			
		}
		rol(tmp, u);
		return tmp;
	}
	
	// XOR from two array and rol and return array	
	public static void SXor(int[][][] k, int[][] s, int u){
		for (int i = 0; i < k.length; i++) {
			for (int j = 0; j < k[i].length; j++) {
				for (int j2 = 0; j2 < k[i][j].length; j2++) {
					k[i][j][j2] = k[i][j][j2]^s[i][j2];
				}
				rol(k[i][j], u);
			}
		}
	}
	// rol 
	public static void rol(int[] arr, int u) {
		while (u>0) {
			int tmp = arr[arr.length-1];
			for (int i = arr.length-1; i > 0; i--) {
				arr[i] = arr[i-1];
			}
			arr[0] = tmp;
			u--;
		}
	}
}
