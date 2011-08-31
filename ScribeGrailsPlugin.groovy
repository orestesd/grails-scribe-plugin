import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.commons.*
import org.scribe.builder.*
import org.scribe.builder.api.*
import org.scribe.model.*
import org.scribe.oauth.*

class ScribeGrailsPlugin {
	
	public static String OAUTHS = 'grails.scribes.plugin.OAUTHS'
	
	def version = "0.1"
	def grailsVersion = "1.3.4 > *"
	def dependsOn = [:]

	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]

	def documentation = "http://grails.org/plugin/grails-scribe-plugin"
	def author = "Orestes Domínguez"
	def authorEmail = ""
	def title = "Grails Scribe Plugin"
	def description = '''\\
This plugin performs scribe integration in grails.
'''

	def onConfigChange = { event ->
	}

	def doWithDynamicMethods = {ctx ->
		// Adding methods / properties to classes
	}
	
	def doWithApplicationContext = { applicationContext ->
		def oauths = readScribeConfig()
		
		applicationContext.servletContext.setAttribute(ScribeGrailsPlugin.OAUTHS, oauths)
	}
	
	private Map readScribeConfig() {
		def scribes = [:]
		
		ConfigurationHolder.config.scribe.each { key, value ->
			def providerName = value?.providerName
			def apiKey = value?.apiKey
			def apiSecret = value?.apiSecret
			def scope = value?.scope
			def callback = getCallback(value?.callback)
			
			println "callback: $callback"
			ServiceBuilder sbuilder = new ServiceBuilder()
				.provider(getApiClassFromName("${providerName}Api"))
				.apiKey("${apiKey}")
				.apiSecret("${apiSecret}")
				.callback(callback)
				
			// scope is present in google provider
			if (scope) 
				sbuilder = sbuilder.scope(scope)
			
				
			scribes[key] = sbuilder.build();
		}
		
		return scribes
		
		/*
		for (c in ApplicationHolder.application.controllerClasses) {
			c.clazz.metaClass.oauths = scribes
		}
		*/
	}
	
	private Class getApiClassFromName(String name) {
		def className = name[0].toUpperCase() + name[1..-1]
		def clazz = Class.forName("org.scribe.builder.api." + className)
		return clazz
	}
	
	private String getCallback(callback = [controller: 'scribe', action:'callback']) {
		String result = getServerUrl(); 
		if (callback instanceof Map) {
			result += "${callback.controller}/${callback.action}"
		} else if (OAuthConstants.OUT_OF_BAND.equals(callback)) {
			return callback
		} else {
			// callback as String
			result += callback 
		}
	}
	
	private String getServerUrl() {
		String serverURL = ConfigurationHolder.config.grails.serverURL.toString()
		serverURL = serverURL.endsWith("/") ? serverURL : serverURL + "/"
		return serverURL
	}

}
