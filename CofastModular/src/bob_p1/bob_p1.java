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
    static double[][] deltaQ1;
    static double[] newDeltaQ1;
    static double[] a_i1;
    static ArrayList<Object> community1;
    static ArrayList<Integer[]> edge_data;
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
        deltaQ1 = (double[][]) Function.getInitV(1).get(0);
        newDeltaQ1 = Function.conNewDelta(deltaQ1);
		a_i1 = (double[]) Function.getInitV(1).get(1);
		community1 = (ArrayList<Object>) Function.getInitV(1).get(2);
		edge_data = (ArrayList<Integer[]>) Function.getInitV(1).get(3);	
		bob_p1 server = new bob_p1(8888);
        server.start();    
       
	}

	public void run() {
	   super.run();
	   try {
	    	socket = server.accept();  
	        do {
	        	k = 0;
	        	h = 1;      	
	        	do {     
		        	if (h >= newDeltaQ1.length) {
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
		        	if (OT.Bconstrut(-(newDeltaQ1[k]-newDeltaQ1[h]), K, N, Integer.parseInt(tmp[2])).equals("B")) {
						k = h;
						sendInforString = "B";
					}
		        	else {
						sendInforString = "A";
					}
		        	h++;
			        new sendMessThread().start(); 	
				} while (true);	        	
	        	if (k == 0) {
    				break;
    			}
            	else {
            		int[] tmp = Function.convert(k, deltaQ1.length);
            		Function.update(deltaQ1, a_i1, community1, tmp[1], tmp[0], edge_data);
            		newDeltaQ1 = Function.conNewDelta(deltaQ1);
            	}
			} while (true);	        
	    } catch (IOException e) {
	        //e.printStackTrace();
	    	System.out.println("Client unconnected");
	    } 
	   for (int i = 0; i < community1.size(); i++) {
		   if (!(community1.get(i) instanceof Integer)) {
				System.out.println(community1.get(i).toString());
		}
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
