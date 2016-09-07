class project_truck {
	//truck details
	public project_driver driver;
	public String truckId;
	public double capacity;
	
	project_truck(){}
	project_truck(project_driver d, String t, double c){
		driver = d;
		truckId = t;
		capacity = c;
	}
	public String getTid(){
		return truckId;
	}
	public double getCapacity(){
		return capacity;
	}
}