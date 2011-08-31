import org.scribe.model.Verifier;

import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

class ScribeService {

	boolean transactional = false

	
	public Token fetchRequestToken() {
		return fetchRequestToken(null);
	}
	
	public Token fetchRequestToken(String providerKey) {
		Token requestToken = getOAuthService(providerKey).getRequestToken();
		return requestToken;
	}
	
	
	
	public Token fetchAccessToken(String requestTokenValue, String verifierValue) {
		return fetchAccessToken(null, requestToken, verifier);
	}
	
	public Token fetchAccessToken(String providerKey, String requestTokenValue, String verifierValue) {
		String secret = fetchRequestToken(providerKey).getSecret()
		Token requestToken = new Token(requestTokenValue, secret)
		Verifier verifier = new Verifier(verifierValue)
		return fetchAccessToken(providerKey, requestToken, verifier);
	}
	
	public Token fetchAccessToken(Token requestToken, Verifier verifier) {
		return fetchAccessToken(null, requestToken, verifier);
	}
	
	public Token fetchAccessToken(String providerKey, Token requestToken, Verifier verifier) {
		Token accessToken = getOAuthService(providerKey).getAccessToken(requestToken, verifier);
		return accessToken;
	}
	
	
	
	public String getAuthorizationUrl(Token requestToken) {
		return getAuthorizationUrl(null, requestToken)
	}
	
	public String getAuthorizationUrl(String providerKey, Token requestToken) {
		return getOAuthService(providerKey).getAuthorizationUrl(requestToken)
	}
	
	
	
	public OAuthService getOAuthService() {
		return getOAuthService(null)
	}
	
	public OAuthService getOAuthService(String name) {
		name = name ?: getDefaultOauthProviderName()
		
		def oauths = ServletContextHolder.servletContext.getAttribute(ScribeGrailsPlugin.OAUTHS)
		def foundEntry = oauths.find { key, value -> key.equals(name)}
		return foundEntry.value
	}
	
	public String getDefaultOauthProviderName() {
		def oauths = ServletContextHolder.servletContext.getAttribute(ScribeGrailsPlugin.OAUTHS)
		def foundEntry = oauths.find { key, value -> return true}
		return foundEntry.key
	}
}
