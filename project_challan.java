class project_challan{
	private String OrderId;
	private String source;
	private String destination;
	private String customerName;
	private String date;
	private String truckId;
	private String driverName;
	private String status;
	private String luggageDetails;
	
	public project_challan() {}
	
	public project_challan(String OI, String s, String d, String cName, String dt, String td, String dN, String st, String lD){
		OrderId = generateOrderId();
		source = s;
		destination = d;
		customerName = cName;
		date = dt;
		truckId = td;
		driverName = dN;
		status = st;
		luggageDetails = lD;
		
	}
	
	// getOid(),getSource(),getDestination(),getCN(),getDate(),getTid(),getDN(),getSt(),getLuggageDetails()
	
	public String toString(){
		return (OrderId+source+destination+customerName+date+truckId+driverName+status);
	}
	
	public void updateSt(String f){
		 status = f;
	}
	public String getSt(){
		 return status;
	}
	public String getTid(){
		return truckId;
	}
	public String getOid(){
		return OrderId;
	}
	public String getDestination(){
		return destination;
	}
	public String getCN(){
		return customerName;
	}
	public String getDate(){
		return date;
	}
	public String getLuggageDetails(){
		return luggageDetails;
	}
	public String getSource(){
		return source;
	}
	public String getDN(){
		return driverName;
	}
	

	public String generateOrderId(){
		return customerName + luggageDetails;
	}
}
