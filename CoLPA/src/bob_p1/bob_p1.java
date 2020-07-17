package bob_p1;

import java.io.BufferedReader;
import function.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;




@SuppressWarnings("unchecked")
public class bob_p1 extends Thread{
		
	ServerSocket server = null;
    Socket socket = null;
    static ArrayList<Object> neighborWeigh;
    static ArrayList<Object> neighbor;
    static ArrayList<Object> node_data;
    static ArrayList<Object> labeList;
    static ArrayList<Object> newLabeList;
    static int[] weigh;
    static int k;
    static int h;
    static String sendInforString;
    
 	public bob_p1(int port) { 		
		try {
		     server = new ServerSocket(port);
		} catch (IOException e) {
		    e.printStackTrace();
		}
 	}
 	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub	
		neighbor = (ArrayList<Object>) Function.getIniteL(0).get(0);	
    	node_data = (ArrayList<Object>) Function.getIniteL(0).get(1);
    	labeList = (ArrayList<Object>) Function.getIniteL(0).get(2);
		bob_p1 server = new bob_p1(8888);
        server.start();    
       
	}

	public void run() {
	   super.run();
	   try {
	    	socket = server.accept();  
	    	int count = 0;
	    	do {
	    		neighborWeigh = Function.comWeigh(node_data, neighbor, labeList);
	    		newLabeList = new ArrayList<Object>();
		    	for (int i = 0; i < node_data.size(); i++) {
		    		weigh = (int[]) neighborWeigh.get(i);
	    			k = 0;
	        		h = 1;   		    	       
		        	do {     
		        		if (h >= weigh.length) {
							break;
						}
				        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				        BufferedReader br = new BufferedReader(isr); 
				        String otherString = "";
				        String[] tmp = new String[2];
				        while (!(otherString = br.readLine().trim()).equals("a")) {		        	
				        	tmp = otherString.split("888888");			        	
						}   
				        int[][][] K = Function.conToMatrix3(tmp[0], 32, 128);
			        	int[] N = Function.conToMatrix(tmp[1], 128);
			        	if (OT.Bconstrut(-(weigh[k]-weigh[h]), K, N, Integer.parseInt(tmp[2])).equals("B")) {
							k = h;
							sendInforString = "B";
						}
			        	else {
			        		sendInforString = "A";
						}
			        	h++;
			        	new sendMessThread().start();  	
		        	}while (true);
            		newLabeList.add(k); 
				}
		    	count ++;	
		    	if (count >= 50) {            		
    				break;
    			}
            	else {
            		Function.update(labeList, newLabeList);
    			}
			} while (true);	  
	    	System.out.println(newLabeList.toString());
	    	ArrayList<Object> community = new ArrayList<Object>();
			for (int i = 0; i < newLabeList.size(); i++) {
				ArrayList<Object> subcommunity = new ArrayList<Object>();
				community.add(subcommunity);
			}
			for (int i = 0; i < newLabeList.size(); i++) {
				if ((int)newLabeList.get(i) != -1) {
					((ArrayList<Object>) community.get((int)newLabeList.get(i))).add(i);
				}
			}
			for (int i = 0; i < community.size(); i++) {
				if (((ArrayList<Integer>) community.get(i)).size()==0) {
					community.remove(i);
					i--;
				}
			}
			for (int i = 0; i < community.size(); i++) {
				System.out.println(community.get(i).toString());
			}
	    } catch (IOException e) {
	        //e.printStackTrace();
	    	System.out.println("Client unconnected");
	    } 
	}
	 
    class sendMessThread extends Thread{
        @Override
        public void run() {
            super.run();
            //String aaString = Function.conSendInfor(deltaQ1);                      
            PrintWriter printWriter = null;
            try {
            	printWriter = new PrintWriter(socket.getOutputStream(),true);              	
            	printWriter.println(sendInforString);
            	printWriter.println("a");
            	printWriter.flush();
            	//printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }          
        }
    }
}
