class Node{
		double arrival;
		double startS;
		double endS;
		double time;
		String eventName;
		Node next;
		
		public Node(double intime, String eName){
			next = null;
			time = intime;
			eventName = eName;
		}
		
		public Node(double arr, double start,double end, String eName){
			next = null;
			arrival = arr;
			startS = start;
			endS = end;
			eventName = eName;
		}
		
		public Node(){
			next = null;
			eventName = "";
		}
		
		public void setNext(Node nxt){
			next = nxt;
		}
	}