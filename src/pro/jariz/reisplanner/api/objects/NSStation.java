package pro.jariz.reisplanner.api.objects;

public class NSStation {
	
	public NSStation() {
		
	}
	
	public String Code;
	public String Type;
	public String Land;
	
	public class Namen {
		public String Kort;
		public String Middel;
		public String Lang;
	}
	public Namen Namen = new Namen();
	
	public Integer UICCode;
	public Double Lat;
	public Double Lon;
	
	public String[] Synoniemen;
	
}