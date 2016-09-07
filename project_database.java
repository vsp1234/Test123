import java.util.*;

class project_database{
	private List<project_truck> truckList;
	private List<project_realTime> realTime;
	private List<project_challan> challanList;
	
	public void loadData() {
		//load drivers and trucks database (static)
		truckList = new ArrayList<project_truck>();
		project_truck refT = new project_truck(new project_driver("Bob"),"123",250.0);
		truckList.add(refT);
		
		//copy all from truckList with status as "Available"
		realTime = new ArrayList<project_realTime>();
		project_truck ref = truckList.get(0);
		realTime.add(new project_realTime("Mumbai","Delhi","Mumbai",ref.truckId, (ref.driver).driverName, ref.capacity,"Available"));
		
		//create challanList
		challanList = new ArrayList<project_challan>();
		
	}
	
	public List<project_truck> getTruckList(){
		return truckList;
	}
	
	public List<project_realTime> getRealTimeList(){
		return realTime;
	}
	public List<project_challan> getChallanList(){
		return challanList;
	}
	
	public void updateRealTime(){
		
	}
}