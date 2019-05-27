package pluralsight.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableResourceServer
public class PluralsightSpringcloudM4SecureserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PluralsightSpringcloudM4SecureserviceApplication.class, args);
	}
	
	@Autowired
	private ResourceServerProperties sso;
	
	@Bean
	public ResourceServerTokenServices myUserInfoTokenServices() {
		return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
	}
	
	@RequestMapping("/tolldata")
	@PreAuthorize("#oauth2.hasScope('toll_read') and hasAuthority('ROLE_OPERATOR')")
	public ArrayList<TollUsage> getTollData() {
		TollUsage instance1 = new TollUsage("100", "station150", "B65Gt1W", "2016-09-20T06:31:22");
		TollUsage instance2 = new TollUsage("101", "station119", "AHY6738", "2016-09-20T05:30:55");
		TollUsage instance3 = new TollUsage("102", "station150", "ZN2GP03", "2016-10-14T016:10:03");

		ArrayList<TollUsage> tolls = new ArrayList<TollUsage>();
		tolls.add(instance1);
		tolls.add(instance2);
		tolls.add(instance3);
		
		return tolls;
	}
	
	public static class TollUsage {
		
		public String id;
		public String stationId;
		public String licensePlate;
		public String timestamp;
		
		public TollUsage() {
		}
		
		public TollUsage(String id, String stationId, String licensePlate, String timestamp) {
			this.id = id;
			this.stationId =stationId;
			this.licensePlate = licensePlate;
			this.timestamp = timestamp;
		}
	}

}
