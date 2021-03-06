package pluralsight.demo;

import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PluralsightSpringcloudM5Dataservice2Application {

	public static void main(String[] args) {
		SpringApplication.run(PluralsightSpringcloudM5Dataservice2Application.class, args);
	}
	
	@Autowired
	private Tracer tracer;
	
	@RequestMapping(value="/customer/{cid}/vehicledetails", method=RequestMethod.GET)
	public @ResponseBody String getCustomerVehicleDetails(@PathVariable Integer cid) throws InterruptedException {
		
		String result ;
		
		Span s = this.tracer.createSpan("lookup Vehicle");
		Span s2 = this.tracer.createSpan("look2");
		
		try{
//			This is valid for one span only and that too the last nested one.In this case s2
			this.tracer.addTag("customerId", cid.toString());
			
			s.logEvent("Database query started");
			
			Thread.sleep(500);
			
			Hashtable<Integer, String> vehicles = new Hashtable<Integer, String>();
			vehicles.put(100, "Lincoln Continental; Plate SNUG30");
			vehicles.put(101, "Chevrolet Camaro; Plate R7TYR43");
			vehicles.put(102, "Volkswagen Beetle; Plate 6CVI3E2");
			
			result = vehicles.get(cid);
			
			s.logEvent("Database query finished");
			
		}finally {
			//spans needs to be closed in reverse order of them created 
			//else it will throw error about not finding parent.
			this.tracer.close(s2);
			this.tracer.close(s);
		}		
		return result;
	}
}
