class project_realTime{
	private String source;
	private String destination;
	private String currentLocation;
	private String truckId;
	private String driverName;
	private double remainingCapacity;
	private String status;
	
	public project_realTime(){}
	public project_realTime(String s,String d,String c,String t, String dN, double rc, String st){
		source = s;
		destination = d;
		currentLocation = c;
		truckId = t;
		driverName = dN;
		remainingCapacity = rc;
		status = st;
	}
	public String getDest(){
		return destination;
	}
	public String getTid(){
		return truckId;
	}
	public String getDN(){
		return driverName;
	}
	public double getRC(){
		return remainingCapacity;
	}
	public void updateRC(double j){
		remainingCapacity-= j;
	}
	public String getStatus(){
		return status;
	}
	public void updateSrc(String f){
		 source = f;
	}
	public void updateDst(String f){
		 destination = f;
	}
	public void updateCl(String f){
		 currentLocation = f;
	}
	public void updateSt(String f){
		 status = f;
	}
	public void resetRC(double d){
		 remainingCapacity = d;
	}
	
	public String toString(){
		return (source+destination+currentLocation+truckId+driverName+remainingCapacity+status);
	}
	
}

// this is bhaskar