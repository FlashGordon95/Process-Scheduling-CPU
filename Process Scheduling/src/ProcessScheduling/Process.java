package ProcessScheduling;


/**
 * @author Ryan Gordon - G00326349
 *
 */
public class Process implements Comparable<Process> {
	//needed for process creations
	private int burst;
	private int initialBurstTime;
	private int pid;
	
	
	//More so used for the algos
	private int startTime;	//The start time is when the process enters the CPU
	private int waitTime;	//Wait time is the amount of time the process needs to wait
	private int processTime; //Process time is the time remaining until the process is completed
	
	
	//Standard constructor for Process
	public Process(int pid, int burst) {
        this.pid = pid;
        this.burst = burst;
        this.initialBurstTime = burst;
    }
	public int getProcessID(){
		return this.pid;
	}
	public int getInitialBurstTime(){
		return this.initialBurstTime;
	}
	public int getBurstTime(){
		return burst;
	}
	public void setBurstTime(int burst){
		this.burst = burst;
	}

	
	@Override
    public int compareTo(Process compareProcess) {
        int compareID=((Process)compareProcess).getProcessID();
        /* For Ascending order*/
        return this.pid-compareID;

        /* For Descending order do like this */
        //return compareID-this.pid;
    }

    @Override
    public String toString() {
        return "[ Process name=" + this.pid + ", Burst=" + this.burst + "]";
    }

}//Process Object
