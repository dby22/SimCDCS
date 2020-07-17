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
    static ArrayList<Object> neighborWeigh;
    static ArrayList<Object> neighbor;
    static ArrayList<Object> node_data;
    static ArrayList<Object> labeList;
    static ArrayList<Object> newLabeList;
    static int[] weigh;
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
    	neighbor = (ArrayList<Object>) Function.getIniteL(1).get(0);
    	node_data = (ArrayList<Object>) Function.getIniteL(1).get(1);
    	labeList = (ArrayList<Object>) Function.getIniteL(1).get(2);
 		alice_p2 client_p2=new alice_p2("127.0.0.1", 8888);
        client_p2.start();
        
	}
    
    @SuppressWarnings("unchecked")
	public void run() {   	   	   
        super.run(); 
        try {
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
            		}while (true);
            		newLabeList.add(k);
        		}
        		count++;
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
        	System.out.println("Server unconnected");
        }
    }
    
    class sendMessThread extends Thread{
        @Override
        public void run() {
	       super.run();
	       //String aaString = Function.conSendInfor(deltaQ2);	       
           PrintWriter printWriter = null;
           try {
        	   ArrayList<Object> sendMenssArrayList = OT.Aconstruct(weigh[k]-weigh[h]);
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