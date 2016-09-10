import java.io.*;
import java.util.*;
import java.sql.*;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

class project_manager
{
	public void startBiz()
	{
		System.out.println("WELCOME TO TRANSPORT MANAGEMENT SYSTEM");
//		try
//		{
//		Runtime.getRuntime().exec("cls");
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
		
		int option;
		String input;
		System.out.println("1. Make Booking");
		System.out.println("2. Update Truck Status"); //send truck or free truck
		System.out.println("3. View Order Status");
		System.out.println("4. Exit");
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{	
			input = reader.readLine();
			option = Integer.parseInt(input);
			
			project_database data = new project_database();
			data.loadData();
			List<project_truck> truckList = data.getTruckList();
			List<project_realTime> realTime = data.getRealTimeList();
			List<project_challan> challanList = data.getChallanList();
			
			switch (option) 
			{
				case 1: makeBooking(realTime, challanList);
						break;
				case 2: updateTruck(truckList, realTime, challanList);
						break;
				case 3: checkStatus(challanList);
						break;
				case 4: System.out.println("Session Ended");
						return;
				default: System.out.println("Invalid input, Start again");
						return;		
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}			
	}
	
	public void makeBooking(List<project_realTime> realTime, List<project_challan> challanList)
	{
		//Get Customer Information and Requirements
		String customerName;
		String source;
		String destination;
		String load;
		String date;
		double weight;
		double remCapacityBeforeNew;
		String allotTruck = "";
		String driverNam = "";
		//Console c = System.console();
		BufferedReader c = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Make a booking:");
		
		try
		{
			
			System.out.println("Customer Name: " );
			customerName = c.readLine();
			System.out.println("Source: ");
			source = c.readLine();
			System.out.println("Destination: " );
			destination = c.readLine();
			System.out.println("Load: " );
			load = c.readLine();
			weight = Double.parseDouble(load); 
		//	System.out.println("Weight: " + weight);
			
			System.out.println("Date: " );
			date = c.readLine(/*"%s","date in dd/mm/yyyy?:"*/);
			
			int flag = 0;
			String query;
			
			PreparedStatement updateQuery = null;
			
			String url = "jdbc:mysql://10.14.4.132 /BHASKARDATABASE";//10.14.5.88:1521
			String user = "sripada";
			String pwd = "bhaskar";
	
			try 
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection(url,user,pwd);
				System.out.println("Connection = " + conn);
				query = "Select * from realTime where (destination = ? AND status = 'Allotted') AND remainingCapacity >= ?";				
				updateQuery = conn.prepareStatement(query);
				updateQuery.setString(1,destination);
				updateQuery.setDouble(2,weight);						
		
				ResultSet rs = updateQuery.executeQuery();
					
				if(rs.next()) 
				{
					allotTruck = rs.getString("truckId");
					System.out.println("First truck Id is - " + allotTruck);
					remCapacityBeforeNew = rs.getDouble("remainingCapacity");
					String updateRt = "UPDATE realTime SET remainingCapacity = ?  WHERE truckId = ? ";
					 
					PreparedStatement updateRealTime = conn.prepareStatement(updateRt);
					updateRealTime.setDouble(1,remCapacityBeforeNew - weight);
					updateRealTime.setString(2,allotTruck);
					flag = 1;
					updateRealTime.executeUpdate();
					System.out.println(flag);
					
					// now updating challan in dbms
					
					project_challan currentChallan = new project_challan ("abc",source,destination,customerName,date,allotTruck,rs.getString("driverName"),"Allotted",load);
					
					query = "INSERT INTO challanList (OrderId , source , destination ,customerName ,currentdate ,truckId , driverName ,status, luggageDetails)  values(?,?,?,?,?,?,?,?,?) ";
					
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
					updateQuery.executeUpdate();		
				}
			
				else if (flag == 0)
				{
					query = "Select * from realTime where status = 'Available' AND remainingCapacity >= ?";
					updateQuery = conn.prepareStatement(query);
					updateQuery.setDouble(1,weight);
					ResultSet rs2 = updateQuery.executeQuery();
					
					if(rs2.next())
					{
						allotTruck = rs2.getString("truckId");
						remCapacityBeforeNew = rs2.getDouble("remainingCapacity");
						driverNam = rs2.getString("driverName");
						String updateRt = "UPDATE realTime SET destination = ?, remainingCapacity = ? , status = 'Allotted' WHERE truckId = ? ";
						 
						PreparedStatement updateRealTime = conn.prepareStatement(updateRt);
						updateRealTime.setString(1,destination);
						updateRealTime.setDouble(2,remCapacityBeforeNew - weight);
						updateRealTime.setString(3,allotTruck);
						flag = 1;
						
						updateRealTime.executeUpdate();
						
						
						project_challan currentChallan = new project_challan("abc",source,destination,customerName,date,allotTruck,driverNam,"Allotted",load);
						
						query = "INSERT INTO challanList (OrderId , source , destination ,customerName ,currentdate ,truckId , driverName ,status, luggageDetails)  values(?,?,?,?,?,?,?,?,?) ";
						
						updateQuery = conn.prepareStatement(query);
						updateQuery.setString(1,currentChallan.getOid());
						updateQuery.setString(2,currentChallan.getSource());
						updateQuery.setString(3,currentChallan.getDestination());
						updateQuery.setString(4,currentChallan.getCN());
						updateQuery.setString(5,currentChallan.getDate());
						updateQuery.setString(6,currentChallan.getTid());
						updateQuery.setString(7,currentChallan.getDN());
						updateQuery.setString(8,currentChallan.getSt());
						updateQuery.setString(9,currentChallan.getLuggageDetails());
						
						updateQuery.executeUpdate();		
					}
					else
					{
					System.out.println("No trucks available");
					}
				}
			}
			catch(SQLException | ClassNotFoundException e)
			{
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
		catch(IOException e)
		{
			e.printStackTrace();
		}		
	}
	
	public void updateTruck(List<project_truck> truckList, List<project_realTime> realTime, List<project_challan> challanList){
		int option;
		String input, tId;
		System.out.println("\n");
		System.out.println("1. Release truck towards destination");
		System.out.println("2. Truck reached destination");
		System.out.println("3. Truck returned to source");
		BufferedReader c = new BufferedReader(new InputStreamReader(System.in));
		//Console c = System.console();
		
		try{
		input = c.readLine();
		option = Integer.parseInt(input);
		

		String url = "jdbc:mysql://10.14.4.132 /BHASKARDATABASE";//10.14.5.88:1521
		String user = "sripada";
		String pwd = "bhaskar";
//		PreparedStatement updateQuery = null;
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url,user,pwd);
		System.out.println("Connection = " + conn);
		switch (option) {
			case 1:
				System.out.println("Truck Id?:");
				tId = c.readLine();
				
				String updateRt = "UPDATE realTime SET status = 'Busy' WHERE truckId = ? "; 
				PreparedStatement updateRealTime = conn.prepareStatement(updateRt);
				updateRealTime.setString(1,tId);
				updateRealTime.executeUpdate();
				
//				System.out.println("YO!!");
				
				String updateChallan = "UPDATE challanList SET status = 'Started'  WHERE truckId = ? AND status = 'Allotted'  "  ; 
				PreparedStatement updateChallanList = conn.prepareStatement(updateChallan);
				updateChallanList.setString(1,tId);
				updateChallanList.executeUpdate();
				
//				System.out.println("YO1!!");
				break;
				
			case 2:
				System.out.println("Truck Id?:");
				tId = c.readLine();
				
				updateRt = "UPDATE realTime rt, trucklist t SET status = 'Returning', rt.remainingCapacity = t.Capacity WHERE rt.truckId = ? ";
				updateRealTime = conn.prepareStatement(updateRt);
				updateRealTime.setString(1,tId);
				updateRealTime.executeUpdate();
				
//				System.out.println("YO!!");
				
				updateChallan = "UPDATE challanList SET status = 'Delivered'  WHERE ((truckId = ?) AND (status != 'Delivered' )) "  ; 
				updateChallanList = conn.prepareStatement(updateChallan);
				updateChallanList.setString(1,tId);
				updateChallanList.executeUpdate();
				
//				System.out.println("YO1!!");
				break;
				
			case 3:
				System.out.println("Truck Id?:");
				tId = c.readLine();
				
				updateRt = "UPDATE realTime rt, trucklist t SET status = 'Available', rt.remainingCapacity = t.Capacity WHERE rt.truckId = ? "; 
				updateRealTime = conn.prepareStatement(updateRt);
				updateRealTime.setString(1,tId);
				updateRealTime.executeUpdate();
				
//				System.out.println("YO!!");
				
				break;
			
		}
		
		}catch(SQLException  | ClassNotFoundException | IOException e){
						e.printStackTrace();
		}
		
		/*switch (option) {
			case 1: tId = c.readLine("%s","Truck Id?:");
					for(project_realTime e: realTime){
						if(e.getTid() == tId)
							e.updateSt("Busy");
					}
					for(project_challan e: challanList){
						if(e.getTid() == tId)
							e.updateSt("Reached");
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
		}*/
	}
	
	public void checkStatus(List<project_challan> challanList){
		String OrderId;
		BufferedReader c = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("OrderId?:");
		try {
		OrderId = c.readLine();
		
		String url = "jdbc:mysql://10.14.4.132 /BHASKARDATABASE";//10.14.5.88:1521
		String user = "sripada";
		String pwd = "bhaskar";
		String query ="";
		PreparedStatement updateQuery = null;
		
		
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Connection = " + conn);
			
			query = "Select * from challanList where OrderId = ? ";
			
			updateQuery =  conn.prepareStatement(query);
			updateQuery.setString(1,OrderId);
			
					
			
			ResultSet rs = updateQuery.executeQuery();
			
			if(rs.next()) 
			{
				System.out.print(rs.getString(1) + " \t");
				System.out.print(rs.getString(2) + " \t");
				System.out.print(rs.getString(3) + " \t");
				System.out.print(rs.getString(4) + " \t");
				System.out.print(rs.getString(5) + " \t");
				System.out.print(rs.getString(6) + " \t");
				System.out.print(rs.getString(7) + " \t");
				System.out.print(rs.getString(8) + " \t");
				System.out.print(rs.getString(9) + " \t");
				
			
			}
			else System.out.println("Ivalid OrderId");
			
			
		}catch(SQLException  | ClassNotFoundException  | IOException e){
			e.printStackTrace();
		}
		
		
		
		/*for(project_challan e: challanList){
			if(e.getOid() == OrderId)
				System.out.println("Order status is "+e.getSt());
		}*/
		
		
		
		
	}
}

