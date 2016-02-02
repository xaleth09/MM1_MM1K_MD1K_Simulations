
public class Queue {
	
	int MAX_SIZE;
	int size;
	Node head;
	Node tail;
	int hasDeath;
	
	public Queue(){
		hasDeath = 0;
		head = null;
		tail = null;
	}
	
	public Queue(int k){
		MAX_SIZE = k;
		hasDeath = 0;
		head = null;
		tail = null;
	}
	
	public void push(){
		Node newNode = new Node();
		if(size == 0){
			head = newNode;
			tail = head;
		}else{
			tail.next = newNode;
			tail = newNode;
		}
		
		size++;
	}
	
	public void push(double time, String eName, int bogus){
		Node newNode = new Node(time, eName);
		if(size == 0){
			head = newNode;
			tail = head;
		}else{
			tail.next = newNode;
			tail = newNode;
		}
		
		size++;
	}
	
	public void push(double a, double s, double e, String eName){
		Node newNode = new Node(a, s, e, eName);
		if(size == 0){
			head = newNode;
			tail = head;
		}else{
			tail.next = newNode;
			tail = newNode;
		}
		
		size++;
	}
	
	
	public int push(double time, String eName){
		Node newNode = new Node(time, eName);
		Node tmp = head;
		
		if(size == MAX_SIZE && MAX_SIZE != 0){
			return -1;
		}
		
		if(size == 0){
			head = newNode;
			tail = head;
		}else if(size == 1){
			if(time >= tmp.time){
				head.next = newNode;
				tail = newNode;
			}else{
				newNode.next = head;
				head = newNode;
				tail = newNode.next;
			}
		}else{
			if(time < head.time){
				newNode.next = head;
				head = newNode;
			}else{
				Node nxtTmp;
				do{
					nxtTmp = tmp.next;
					if(time <= nxtTmp.time){
						newNode.next = nxtTmp;
						tmp.next = newNode;
						size++;
						return 0;
					}
					tmp = tmp.next;
				}while(nxtTmp.next != null);
				tail.next = newNode;
				tail = newNode;
			}
			
		}
		
		size++;
		return 0;
	}
	
	public Node pop(){
		if(size == 0){
			System.out.println("CAN'T POP NOTHING IN HERE");
			Node shit = new Node(0,"NOTHING");
			return shit;
		}
		Node ret;
		ret = head;
		head = head.next;
		ret.next = null;
		size--;
		
		return ret;
	}
	
	public void printq(){
		Node tmp = head;
		if(tmp == null){
			System.out.println("Queue is empty!");
			return;
		}
		
		while(tmp != null){
			System.out.print(tmp.eventName);
			System.out.print(": ");
			System.out.print(tmp.time);
			System.out.print(", ");
			tmp = tmp.next;
		}
		System.out.println();
	}
	
	public void printcq(){
		Node tmp = head;
		if(tmp == null){
			System.out.println("Queue is empty!");
			return;
		}
		
		int i = 0;
		while(tmp != null){
			System.out.print("c ");
			System.out.print(i++);
			System.out.print(", ");
			tmp = tmp.next;
		}
		System.out.println();
	}
	
	
}
