package project.dat250.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import project.dat250.entity.User;

@Controller
public class mainRestController {

	/**
	 * Returns user ID for given client
	 * 
	 * @param client with logged in user
	 * @return uID of the logged in user
	 */
	public String getUser(OAuth2AuthorizedClient client) {
		String uID = "";
		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
			HttpEntity entity = new HttpEntity("", headers);
			ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity,
					Map.class);
			Map userAttributes = response.getBody();

			if (userAttributes.containsKey("sub"))
				uID = userAttributes.get("sub").toString();
			else
				uID = userAttributes.get("id").toString();
		}
		return uID;
	}

	/**
	 * Generates a User entity from given logged in client
	 * 
	 * @param client with logged in user
	 * @return User that has to be added/checked to database
	 */
	public User saveUser(OAuth2AuthorizedClient client) {
		User newUser = new User();

		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
			HttpEntity entity = new HttpEntity("", headers);
			ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity,
					Map.class);
			Map userAttributes = response.getBody();

			String[] name = userAttributes.get("name").toString().split(" ");
			if (userAttributes.containsKey("sub")) // Deciding if user logged in with google or facebook
				newUser.setUID(userAttributes.get("sub").toString());
			else
				newUser.setUID(userAttributes.get("id").toString());
			newUser.setFname(name[0]);
			if (name.length > 1) // Checking if user has set Last name on his google/facebook profile
				newUser.setLname(name[1]);
			else
				newUser.setLname(" ");
			newUser.setEmail(userAttributes.get("email").toString());
			newUser.setAdmin(false);
		}
		return newUser;
	}
}
