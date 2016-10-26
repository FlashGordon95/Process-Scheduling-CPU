package ProcessScheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.BinaryOperator;

import javax.swing.JOptionPane;


public class Runner {
	/*
	 * This program simulates a CPU scheduler 
	 * In a CPU scheduler different processes will require the CPU at a given time for computation
	 * There are different ways this can be implemented
	 * FCFS - First Come First Served
	 * SJF - Shortest Job First
	 * RR - Round Robin
	 * 
	 * Within the program there are facilities for all 3 scheduling algorithms.
	 * Each process receives a time in units e.g 1; 34; 545; this can represent time, memory whatever you like
	 * @Author - Ryan Gordon - G00326349
	 */
	 
	public static void main(String[] args) 
	{
		Scanner console = new Scanner(System.in); //scanner for input
		
		List<Process> processArray = new ArrayList<>(); //This will hold our Process Objects
		int totalProcesses;
		int userOption;
				
		//Gather total amount of processes
		System.out.println("Enter the total number of processes: ");
		totalProcesses = console.nextInt();
		
		//for loop which prompts for a burst time
		//we then add the process into the arrayList and move to next iteration
		for(int i = 1; i <= totalProcesses; i++)
		{
			//Get the burst times for each process
			System.out.println("\nEnter the burst times for process "+i+": ");
			processArray.add(new Process(i, console.nextInt()));
		}
		//will run code inside at least once and then evaluate the while condition
		do
		{
		
		//Print the choices and prompt the user to enter an option
		System.out.println("\n1: FCFS - First Come First Serve \n2: SJF - Shortest Job First \n3: Round Robin \n4: Exit");
		System.out.println("Please choose a scheduling algorithm: ");
		userOption = console.nextInt();
		
		
			switch(userOption)
			{
				case 1:
					//will call the fcfs method which takes in the processes and also the number of the processes
					//fcfs will attempt to complete each job in its order
					
					fcfs(processArray, totalProcesses);
					break;
				case 2:
					//Collections.sort allows us to reorder the process array by a parameter
					//In this case it is ordered by the burst time
					//This uses the compareTo() method in the PiD class which has been overridden
					Collections.sort(processArray);
					fcfs(processArray, totalProcesses);
					//to be added : after sorting the array we can simply reuse FCFS
					break;
				case 3:
					//Prompts for a quantum time and then begins scheduling via RR
					//Gives each process n time in the CPU and then takes another
					//Prevents any one process not getting any process time
					roundRobin(processArray, totalProcesses);
					break;
				default:
					System.out.println("No Process selected, ending program...");
					break;
			}
			//Upon completion of the chosen algo, we notify user
			//Key prompt is asked here to prevent the loop for repeating endlessly
			System.out.println("Scheduling processes has completed");
			System.out.println("Press any key to continue...");
			console.next();

		}while(userOption != 4); // 4 terminates loops
		
	} //end main()
	
	public static void fcfs (List<Process> processArray, int totalProcesses)
	{
		int totalWaitTime = 0;
		float averageWaitTime;
		
		System.out.println("Process No | Initial Time | Burst Time | Wait Time");
		
		int[] waitingTime = new int[totalProcesses];
		//Initialize a counter variable to 0
		int count = 0;
		
		//Loop through the ArrayList and add up the wait time
		for(Process process : processArray)
		{
			//print the process information
			System.out.printf("%5d %15d %12d %11d\n", process.getProcessID(), process.getInitialBurstTime(), process.getBurstTime(), waitingTime[count]);
			
			//increment the counter to calculate the wait time for the process
			//the wait time on the first process will always be 0
			count++;
			//if the counter is less than the number of processes then add to the wait time
			if(count < totalProcesses)
			{
				//The wait time = the previous wait time + the current burst time
				waitingTime[count] = waitingTime[count - 1] + process.getBurstTime();
			}
			
		} //end for
		
		//Calc total waiting time
		for(int i = 0; i < totalProcesses; i++)
		{
			totalWaitTime += waitingTime[i];
		}
		System.out.println("\nThe total wait time = " + totalWaitTime);
		//get average waiting time
		averageWaitTime = (float)totalWaitTime / totalProcesses;
		System.out.println("\nThe average wait time = " + averageWaitTime);
	} //end fcfs()
	public static void roundRobin(List<Process> processArray, int totalProcesses){
		BinaryOperator<Integer> integerAdder = (i, j) -> i + j; // operator used for the streams below
		Scanner console = new Scanner(System.in);
		int quantum;	//time each process can have in the cpu in their cycle of the rr
		int totalWaitTime = 0;
		System.out.println("Please enter a quantum time: ");
		quantum = console.nextInt();
		
		//Java 8 Streams implementation.
		//Here is make use of Streams and in particular the reduction features
		//Adapted from https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
		// - Ryan Gordon
		Integer totalBurstReduce =  processArray
				.stream()
				.map(Process::getBurstTime)
				.reduce(0, integerAdder);
		
		System.out.println("Total process time of all processes :"+totalBurstReduce);
		System.out.println("Process No | Initial Time | Remaining Time | Start Time | Burst Time | Wait Time");
		
		int startTime = 0;
		int burstTime = 0;
		int[] waitingTime = new int[totalProcesses];
		int[] timeSpent = new int[totalProcesses];
		int i = 0;
		
		//while totalBurstReduce > 0 loop through the array and attempt to complete processes
		while(totalBurstReduce > 0)
		{
			i = 0;
			for(Process process : processArray)
			{
				if(process.getBurstTime() < quantum)
				{
					burstTime = process.getBurstTime(); 
					if(process.getBurstTime()==0){
			
					}
					else if (i==(waitingTime.length-1)){
						waitingTime[0] = waitingTime[0] + (process.getBurstTime()*(waitingTime.length-1));

					}
					else{
						waitingTime[i+1] = waitingTime[i] + (process.getBurstTime()*(waitingTime.length-1));
					}
					process.setBurstTime(0); //process is finished 
					//increment timeSpent running for each process
					timeSpent[i] += burstTime;
				}//end if
				else
				{
					burstTime = quantum;
					if(process.getBurstTime()==0){
						
					}
					else if (i==(waitingTime.length-1)){
						waitingTime[0] = waitingTime[0] + (quantum*(waitingTime.length-1));
					}
					else{
						waitingTime[i+1] = waitingTime[i] + (quantum*(waitingTime.length-1));
					}
					process.setBurstTime(process.getBurstTime() - quantum);
					//increment timeSpent running for each process
					timeSpent[i] += burstTime;
				}//end else
				
				//if else ladder used to calculate the waiting times
				if(timeSpent[i] > 0 && i == 0)
				{
					//if the time spent is greater than 0 and the processNo is 1
					waitingTime[i] = startTime + burstTime - timeSpent[i];
				}
				else if(i > 0 && timeSpent[i] <= quantum && burstTime > 0)
				{
					//if the the processNo is greater than 1 AND
					waitingTime[i] = startTime;
				}
				else if (burstTime > 0)
				{
					//if the burstTime is > 0
					//but the process time spent is greater than the quantum
					waitingTime[i] = startTime + burstTime - timeSpent[i];
				}
				else if (burstTime == 0)
				{
					//if the burstTime is = 0 then the process has finished
					//the wait time remains the same
					waitingTime[i] = waitingTime[i];
				}
				
				System.out.printf("%5d %15d %16d %12d %12d %11d\n", process.getProcessID(), process.getInitialBurstTime(), process.getBurstTime(), startTime, burstTime, waitingTime[i]);
				
				startTime += burstTime; //add to start time for next variable
				totalBurstReduce = totalBurstReduce - burstTime; //Reduce totalBurstReduce
				i++;
			} //end for
			//Placeholder text 
			System.out.println("<==============================================================================>");
		} //end while
		//Calc total waiting time
		for(int j = 0; j < totalProcesses; j++)
		{
			totalWaitTime += waitingTime[j];
		}
		System.out.println("\nThe total wait time = " + totalWaitTime);
		//get average waiting time
		float averageWaitTime = (float)totalWaitTime / totalProcesses;
		System.out.println("\nThe average wait time = " + averageWaitTime);

	}//end roundRobin()


}
