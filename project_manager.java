import java.io.*;
import java.util.*;
import java.sql.*;

class project_manager{
	public void startBiz(){
		int option;
		String input;
		System.out.println("1. Make Booking");
		System.out.println("2. Update Truck Status"); //send truck or free truck
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
		double remCapacityBeforeNew;
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
		String query;
		
		PreparedStatement updateQuery = null;
		
		
		
		
		
		String url = "jdbc:mysql://10.14.4.132 /USER12";//10.14.5.88:1521
		String user = "sripada";
		String pwd = "bhaskar";
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Connection = " + conn);
			query = "Select * from realTime where destination = ? AND status = 'Alloted'  AND remainingCapacity >= ?";
			
			updateQuery =  conn.prepareStatement(query);
			updateQuery.setString(1,destination);
			
			updateQuery.setDouble(2,weight);
			
			
			ResultSet rs = updateQuery.executeQuery();
			
			if(rs.next()) 
			{
				allotTruck = rs.getString("truckId");
				remCapacityBeforeNew = rs.getDouble("remainingCapacity");
				String updateRt = "UPDATE realTime SET remainingCapacity = ?  WHERE truckId = ? ";
				 
				PreparedStatement updateRealTime = conn.prepareStatement(updateRt);
				updateRealTime.setDouble(1,remCapacityBeforeNew - weight);
				updateRealTime.setString(2,allotTruck);
				flag =1;
				
				// now updating challan in dbms
				
				project_challan currentChallan = new project_challan("abc",source,destination,customerName,date,allotTruck,rs.getString("driverName"),"Allotted",load);
				
				query = "INSERT INTO challanList(OrderId , source , destination ,customerName ,currentdate ,truckId , driverName ,status, luggageDetails)  values(?,?,?,?,?,?,?,?,?) ";
				
				updateQuery =  conn.prepareStatement(query);
//				,,,getDate(),getTid(),getDN(),getSt(),get
				updateQuery.setString(1,currentChallan.getOid());
				updateQuery.setString(2,currentChallan.getSource());
				updateQuery.setString(3,currentChallan.getDestination());
				updateQuery.setString(4,currentChallan.getCN());
				updateQuery.setString(5,currentChallan.getDate());
				updateQuery.setString(6,currentChallan.getTid());
				updateQuery.setString(7,currentChallan.getDN());
				updateQuery.setString(8,currentChallan.getSt());
				updateQuery.setString(9,currentChallan.getLuggageDetails());
				
				updateQuery.executeQuery();
				
				
				
				
			}
			
			else if (flag == 0){
				query = "Select * from realTime AND status = 'Available'  AND remainingCapacity >= ?";
				
				updateQuery =  conn.prepareStatement(query);
				updateQuery.setDouble(1,weight);
				ResultSet rs2 = updateQuery.executeQuery();
				
				if(rs2.next()){
					allotTruck = rs2.getString("truckId");
					remCapacityBeforeNew = rs2.getDouble("remainingCapacity");
					String updateRt = "UPDATE realTime SET remainingCapacity = ? , status = 'Alloted'  WHERE truckId = ? ";
					 
					PreparedStatement updateRealTime = conn.prepareStatement(updateRt);
					updateRealTime.setDouble(1,remCapacityBeforeNew - weight);
					updateRealTime.setString(2,allotTruck);
					
					flag =1;
					
					project_challan currentChallan = new project_challan("abc",source,destination,customerName,date,allotTruck,rs.getString("driverName"),"Allotted",load);
					
					query = "INSERT INTO challanList(OrderId , source , destination ,customerName ,currentdate ,truckId , driverName ,status, luggageDetails)  values(?,?,?,?,?,?,?,?,?) ";
					
					updateQuery =  conn.prepareStatement(query);
					updateQuery.setString(1,currentChallan.getOid());
					updateQuery.setString(2,currentChallan.getSource());
					updateQuery.setString(3,currentChallan.getDestination());
					updateQuery.setString(4,currentChallan.getCN());
					updateQuery.setString(5,currentChallan.getDate());
					updateQuery.setString(6,currentChallan.getTid());
					updateQuery.setString(7,currentChallan.getDN());
					updateQuery.setString(8,currentChallan.getSt());
					updateQuery.setString(9,currentChallan.getLuggageDetails());
					
					updateQuery.executeQuery();
					
				}
				
				else{
					System.out.println("No trucks available");
				}
				
				
			}
		
		
		}catch(SQLException | ClassNotFoundException e){
		e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
	/*
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
		
				
		*/
		
		
		
		
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

//bhaskar edit


	