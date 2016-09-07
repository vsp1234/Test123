import java.io.*;
import java.util.*;

class project_manager{
	public void startBiz(){
		int option;
		String input;
		System.out.println("1. Make Booking");
		System.out.println("2. Update Tracking Status"); //send truck or free truck
		System.out.println("3. View Order Status");
		System.out.println("4. Exit");
		Console c = System.console();
		input = c.readLine("%s","option?:");
		option = Integer.parseInt(input);
		
		project_database data = new project_database();
		data.loadData();
		List<project_truck> truckList = data.getTruckList();
		List<project_realTime> realTime = data.getRealTimeList();
		List<project_challan> challanList = data.getChallanList();
		
		switch (option) {
			case 1: makeBooking(realTime, challanList);
					break;
			case 2: updateTracker(truckList, realTime, challanList);
					break;
			case 3: checkStatus(challanList);
					break;
			case 4: System.out.println("Session Ended");
					return;
			default: System.out.println("Invalid input, Start again");
					return;
				
		}
		
	}
	
	public void makeBooking(List<project_realTime> realTime, List<project_challan> challanList){
		//Get Customer Information and Requirements
		String customerName;
		String source;
		String destination;
		String load;
		String date;
		double weight;
		String allotTruck = "";
		Console c = System.console();
		System.out.println("");
		customerName = c.readLine("%s","customerName?:");
		source = c.readLine("%s","source?:");
		destination = c.readLine("%s","destination?:");
		load = c.readLine("%s","weight in tons?:");
		weight = Double.parseDouble(load); 
		date = c.readLine("%s","date in dd/mm/yyyy?:");
		
		int flag = 0;
		for(project_realTime e: realTime){
			if(e.getDest() == destination && e.getStatus() == "Allotted" && e.getRC() >= weight) {
				allotTruck = e.getTid();
				e.updateRC(weight);
				challanList.add(new project_challan(customerName+load,source,destination,customerName,date,allotTruck,e.getDN(),"Allotted",load));
				flag = 1;
			}
		}
		if(flag == 0){
			for(project_realTime e: realTime){
			if(e.getStatus() == "Available" && e.getRC() >= weight) {
				allotTruck = e.getTid();
				e.updateSrc(source);
				e.updateDst(destination);
				e.updateCl(source);
				e.updateRC(weight);
				e.updateSt("Allotted");
				challanList.add(new project_challan(customerName+load,source,destination,customerName,date,allotTruck,e.getDN(),"Allotted",load));
				System.out.println(realTime);
				System.out.println(challanList);
				flag = 2;
				break;
			}
		}
		if(flag == 0)
			System.out.println("No trucks available");
		}
		
		
	}
	
	public void updateTracker(List<project_truck> truckList, List<project_realTime> realTime, List<project_challan> challanList){
		int option;
		String input, tId;
		System.out.println("");
		System.out.println("1. Send a Truck");
		System.out.println("2. Make a Truck available");
		
		Console c = System.console();
		input = c.readLine("%s","option?:");
		option = Integer.parseInt(input);
		
		switch (option) {
			case 1: tId = c.readLine("%s","Truck Id?:");
					for(project_realTime e: realTime){
						if(e.getTid() == tId)
							e.updateSt("Busy");
					}
					for(project_challan e: challanList){
						if(e.getTid() == tId)
							e.updateSt("Busy");
					}
					break;
			case 2: tId = c.readLine("%s","Truck Id?:");
					double capacity=0.0;
					for(project_truck e: truckList){
						if(e.getTid() == tId)
							capacity = e.getCapacity();
					}
					for(project_realTime e: realTime){
						if(e.getTid() == tId)
							e.updateSt("Available");
							e.resetRC(capacity);
					}
					for(project_challan e: challanList){
						if(e.getTid() == tId)
							e.updateSt("Reached");
					}			
					break;
			default:
		}
	}
	
	public void checkStatus(List<project_challan> challanList){
		String OrderId;
		Console c = System.console();
		OrderId = c.readLine("%s","OrderId?:");
		
		for(project_challan e: challanList){
			if(e.getOid() == OrderId)
				System.out.println("Order status is "+e.getSt());
		}
		
	}
}


	