package pluralsight.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableOAuth2Sso
public class ReportController extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2ClientContext clientContext;
	
	@Autowired
	private OAuth2RestTemplate oAuth2RestTemplate;
	
	@RequestMapping("/reports")
	public String loadReports(Model model) {
		OAuth2AccessToken t = clientContext.getAccessToken();
		System.out.println("Token: " + t.getValue());
		
		ResponseEntity<ArrayList<TollUsage>> tolls = oAuth2RestTemplate.exchange("http://localhost:9001/services/tolldata", HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<TollUsage>>() {
		});
		
		model.addAttribute("tolls", tolls.getBody());
		
		return "reports";
	}
	
	@RequestMapping("/tolldata")
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
	
	@RequestMapping("/")
	public String loadHome() {
		return "home";
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/login**")
			.permitAll()
			.anyRequest()
			.authenticated();
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
