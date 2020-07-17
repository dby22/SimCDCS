package alice_p2;

import java.io.BufferedReader;
import function.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class alice_p2 extends Thread{

    Socket socket = null;
    static double[][] deltaQ2;
    static double[] new_deltaQ2;
    static double[] a_i2;
    static ArrayList<Object> community2;
    static ArrayList<Integer[]> edge_data;
    static int k;
    static int h;
    
    public alice_p2(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException {
    	deltaQ2 = (double[][]) Function.getInitV(2).get(0);
    	new_deltaQ2 = Function.conNewDelta(deltaQ2);
 		a_i2 = (double[]) Function.getInitV(2).get(1);
 		community2 = (ArrayList<Object>) Function.getInitV(2).get(2);
 		edge_data = (ArrayList<Integer[]>) Function.getInitV(2).get(3);	
 		alice_p2 client_p2=new alice_p2("127.0.0.1", 8888);
        client_p2.start();
        
	}
    
    public void run() {   	   	   
        super.run(); 
        try {
        	do {
        		k = 0;
        		h = 1;	
        		do {
            		if (h >= new_deltaQ2.length) {
    					break;
    				}
    				new sendMessThread().start();        		 
    				InputStreamReader isr=new InputStreamReader(socket.getInputStream());
    				BufferedReader br = new BufferedReader(isr);                 
    				String otherString = "";
    				while (!(otherString = br.readLine().trim()).equals("a")) {
    					if (otherString.equals("B")) {
    							k=h;
    					}
    				}  
    				h++;		 
        		} while (true);
            	if (k == 0) {
    				break;
    			}
            	else {
            		int[] tmp = Function.convert(k, deltaQ2.length);
                	Function.update(deltaQ2, a_i2, community2, tmp[1], tmp[0], edge_data);
                	new_deltaQ2 = Function.conNewDelta(deltaQ2);
    			}
			} while (true);       	
        } catch (IOException e) {
        	System.out.println("Server unconnected");
        }
        for (int i = 0; i < community2.size(); i++) {
        	 if (!(community2.get(i) instanceof Integer)) {
 				System.out.println(community2.get(i).toString());
        	 }		
        }
    }
    
    class sendMessThread extends Thread{
        @Override
        public void run() {
	       super.run();
	       //String aaString = Function.conSendInfor(deltaQ2);	       
           PrintWriter printWriter = null;
           try {
        	   ArrayList<Object> sendMenssArrayList = OT.Aconstruct(new_deltaQ2[k]-new_deltaQ2[h]);
    		   String sendInforString1 = Function.conSendInfor1((int[][][])sendMenssArrayList.get(0));
        	   String sendInforString2 = Function.conSendInfor2((int[])sendMenssArrayList.get(1));
        	   String sendInforString3 = Integer.toString((int) sendMenssArrayList.get(2));
        	   String sendInforString = sendInforString1 + "888888" + sendInforString2 + "888888" + sendInforString3;
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