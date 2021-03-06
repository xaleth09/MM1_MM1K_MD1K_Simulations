import java.io.*;
import java.math.*;
import java.util.Random;

public class controller {
	//record Keeping vars
	double Tq;
	double Tw;
	int q;
	int w;
	int numMons;
	int numRqs;
	
	//useful vars
	double currentTime;
	double servTime;

	double lambda;
	Random rnd;
	Queue eventsQ;
	Queue customersQ;
	Queue tQ;
	Queue tWQ;
	Queue cStartEndQ;
	
	public controller(double inLam, double inTs, int maxQSize){
		rnd = new Random();
		currentTime = 0;
		servTime = inTs;
		lambda = inLam;
		eventsQ = new Queue();
		customersQ = new Queue(maxQSize);
		tQ = new Queue();
		tWQ = new Queue();
		cStartEndQ = new Queue();
		
	}

	
	public double nextTime(double lmda){
		
		double res = -Math.log(1.0 - rnd.nextDouble()) / lmda;
		
		return res;
	}
	
	public static void main(String[] args){
		controller c = new controller(65, .03, 3);
		double runTime = 200;
		
		int maxQsize = 0;
		Node curEvent;
		Node poppedC;
		double starttime;
		double nextArr = c.nextTime(c.lambda);
		c.eventsQ.push(nextArr,"birth");
		c.eventsQ.push(nextArr + c.servTime, "death");
		c.eventsQ.hasDeath = 1;
		int i = 0;
		boolean firstMon = true;
		System.out.println("~~~~~~~STARTING~~~~~~~~");
		while(c.currentTime < runTime){
			//adds first monitor after reaching steady state
			if(c.currentTime > runTime/2-1 && firstMon){
				c.eventsQ.push(c.nextTime(c.lambda), "monitor");
				firstMon = false;
			}
			//get nextEvent
			curEvent = c.eventsQ.pop();
			//if birth
			if(curEvent.eventName.equals("birth")){
				//System.out.print("BIRTH @  ");
				c.currentTime = curEvent.time;
				int success = c.customersQ.push(curEvent.time, "customer"); //put customer in queue
				nextArr = curEvent.time + c.nextTime(c.lambda);
				c.eventsQ.push(nextArr, "birth");
				//put death in queue if no death is scheduled and customerQ only has one 1 customer (idle)
				if(c.customersQ.size == 1 && c.eventsQ.hasDeath == 0 && success != -1){
					c.eventsQ.push(curEvent.time + c.servTime, "death");
				}
			
				
			//if death	
			}else if(curEvent.eventName.equals("death")){
				//System.out.print("DEATH @  ");
				c.eventsQ.hasDeath = 0;
				starttime = c.currentTime;
				c.currentTime = curEvent.time;
				poppedC = c.customersQ.pop(); //remove customer
				//death start = starttime, death end = curEvent.time
				//event arrival time = poppedC.time
				if(c.currentTime > runTime/2-1){ 
					c.cStartEndQ.push(poppedC.time, starttime, curEvent.time, "rKeeper");
					c.tWQ.push(starttime-poppedC.time,"tw", 0); //time in queue, time between starttime and time it entered queue
					c.tQ.push(curEvent.time - poppedC.time  ,"tq", 0); //turnaroundTime = time death ends (happens)  - time entered queue
					c.numRqs++;
				}
				//if death occurred and there are still customers in Q schedule next death
				if(c.customersQ.size != 0){
					c.eventsQ.push(curEvent.time + c.servTime, "death");
					c.eventsQ.hasDeath = 1;
				}
				
			//if monitor
			}else if(curEvent.eventName.equals("monitor") && c.currentTime > runTime/2-1){
				c.currentTime = curEvent.time;
				c.w += c.customersQ.size;
				c.q += c.customersQ.size;
				if(c.eventsQ.head.eventName.equals("death")){
					c.q++;
				}
				c.numMons++;
				c.eventsQ.push(c.nextTime(c.lambda), "monitor");
			}
	
			
		}//end while
		
		//LOG PRINTING TO CONSOLE
		int wAvg = c.w/c.numMons;
		int qAvg = c.q/c.numMons;
		System.out.printf("Lambda = %.2f, Ts = %.3f, SimTime = %.2f\n\n", c.lambda, c.servTime, runTime);
		System.out.printf("wAvg = %d, qAvg = %d\n", wAvg, qAvg);
		Node tmp = c.cStartEndQ.head;
		Node tmp2 = c.tQ.head;
		Node tmp3 = c.tWQ.head;
		System.out.println("Arr      Tw      S       E        tQ");
		double tWAvg = 0;
		double tQAvg = 0;
		int t = c.cStartEndQ.size/4;
		while(tmp!=null && t != 0){
			System.out.printf("%.3f   %.3f   %.3f   %.3f   %.3f\n", tmp.arrival, tmp3.time, tmp.startS, tmp.endS, tmp2.time);
			t--;
			tWAvg += tmp3.time;
			tQAvg +=tmp2.time;
			tmp = tmp.next;
			tmp2 = tmp2.next;
			tmp3 = tmp3.next;
		}
		tWAvg = tWAvg/(c.cStartEndQ.size/4);
		tQAvg = tQAvg/(c.cStartEndQ.size/4);
		System.out.printf("Tw(avg) = %.3f, Tq(avg) = %.3f", tWAvg, tQAvg);
		
	}//end main
}

