package grails.scribe.plugin

import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.model.Verifier

class ScribeController {

	def scribeService
	
    def index = { 
	}
	
	def auth = {
		log.info "Prepare for open authorisation..."

        // Get consumer name from auth request
        String consumerName = params?.consumer ?: session?.cbparams?.consumer
        String errorController = params?.error_controller ?: session?.cbparams?.error_controller
        String errorAction = params?.error_action ?: session?.cbparams?.error_action
        String errorId = params?.error_id ?: session?.cbparams?.error_id

        try {
            /*
             * Some services like FireEagle don't retain callback and params.
             * Must store the params in session.
             */
            params?.remove('controller')
            params?.remove('action')
            session.cbparams = params

            Token token = scribeService.fetchRequestToken(consumerName)
            session.oauthToken = token
			log.info "Stored token to session: ${session.oauthToken}"
            
			String authURL = scribeService.getAuthorizationUrl(token)

            log.info "Going to redirect to auth url: $authURL"
            redirect(url: authURL)

        } catch (Exception ose) {
            log.error "Unable to initialise authorization: $ose"

            flash.oauthError = message(code: "oauth.requesttoken.missing",
                default: "Failed to retrieve the request token from the OAuth service provider. " +
                    "Please try to the authorization action again.")
            redirect(controller: errorController, action: errorAction, id: errorId)
        }
	}
	
	def callback = {
		log.info "Callback received..."
		log.info "Got callback params: $params"

		// List session parameters
		log.info "Session parameters:"
		session.cbparams.each{ k, v ->
			log.info "- $k: $v"
		}

		// Get required redirect controllers and actions
		String returnController = params?.remove('return_controller') ?: session?.cbparams?.remove('return_controller')
		String returnAction = params?.remove('return_action') ?: session?.cbparams?.remove('return_action')
		String returnId = params?.remove('return_id') ?: session?.cbparams?.remove('return_id')
		String errorController = params?.remove('error_controller') ?: session?.cbparams?.remove('error_controller')
		String errorAction = params?.remove('error_action') ?: session?.cbparams?.remove('error_action')
		String errorId = params?.remove('error_id') ?: session?.cbparams?.remove('error_id')

		// Clean up
		params?.remove('controller')
		params?.remove('action')

		//  List remaining parameters
		log.info "Remaining parameters:"
		params.each { k, v ->
			log.info "- $k: $v"
		}

		// Update request parameters with session
		final def redirParams = params + session.cbparams

		// List re-direct parameters
		log.info "Re-direct parameters:"
		redirParams?.each{ k, v ->
			log.info "- $k: $v"
		}

		// Kill session parameters
		session.cbparams = null

		def oauth_token = params?.oauth_token
		if (oauth_token && oauth_token != session.oauthToken.token) {
			// Returned token is different from the last received request token
			flash.oauthError = message(code: "oauth.token.mismatch",
				default: "There has been an error in the OAuth request. Please try again.")
			redirect(controller: errorController, action: errorAction, id: errorId,
				params: redirParams)
			return
		}

		// OAuth 1.0a
		def oauth_verifier = params[OAuthConstants.VERIFIER]

		try {
			String consumerKey = redirParams?.consumer ?: scribeService.getDefaultOauthProviderName()
			Token token = session?.oauthToken
			Verifier verifier = new Verifier(oauth_verifier)
			
			Token accessToken = scribeService.fetchAccessToken(consumerKey, token, verifier)
			session[consumerKey]?.accessToken = accessToken

			log.info("Got access token: ${accessToken?.key}")
			log.info("Got token secret: ${accessToken?.secret}")
			log.info("OAuth Verifier: ${oauth_verifier}")
			log.info("Saved token to session: [key]${session?.oauthToken?.key} " +
				"[secret]${session?.oauthToken?.secret} " +
				"[verifier]${session?.oauthToken?.verifier} ")
			log.info "Redirecting: [controller]$returnController, [action]$returnAction\n"

						redirect(controller: returnController, action: returnAction, id: returnId,
				params: redirParams)
			
		} catch (Exception ose) {
			log.error "Unable to fetch access token: $ose"
			
			flash.oauthError = message(code: "oauth.400badrequest",
				default: "There has been an error in the OAuth request. Please try again.")
			redirect(controller: errorController, action: errorAction, params: redirParams)
		}
	}
	
	def complete = {
		return  "complete!!"
	}
	
	def error = {
		return "¡¡¡ ERROR !!"
	}
}
