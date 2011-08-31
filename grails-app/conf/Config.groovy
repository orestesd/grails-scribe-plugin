scribe {
	
//	facebook {
//		providerName = 'Facebook'
//		apiKey = '105620528613'
//		apiSecret = '593858e39e2b4fbe0e94a36a5cf9d9b3'
//	}
	
	twitter {
		providerName = 'Twitter'
		apiKey = 'HMGtzjcxvBbHYQ3927uz9Q'
		apiSecret = 'smbl5EtI59wmUygBDNJVqI8uSLiNwxb0068LM0oRBPs'
		callback = [controller:'scribe', action:'callback']
	}
	
//	linkedIn {
//		providerName = 'LinkedIn'
//		apiKey = 'CiEgwWDkA5BFpNrc0RfGyVuSlOh4tig5kOTZ9q97qcXNrFl7zqk-Ts7DqRGaKDCV'
//		apiSecret = 'dhho4dfoCmiQXrkw4yslork5XWLFnPSuMR-8gscPVjY4jqFFHPYWJKgpFl4uLTM6'
//	}
//	
//	google {
//		providerName = 'Google'
//		apiKey = 'anonymous'
//		apiSecret = 'anonymous'
//		scope = 'https://docs.google.com/feeds/'
//	}

}



environments {
	production {
		grails.serverURL = "http://www.produccion.com"
	}
	development {
		grails.serverURL = "http://88.16.55.186:8080/${appName}"
	}
	test {
		grails.serverURL = "http://test:8080/${appName}"
	}

}


log4j = {
	// Example of changing the log pattern for the default console
	// appender:
	//
	//appenders {
	//    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
	//}

	info   'grails.app.controller', //  controllers
		   'grails.app.service'		//  services
	
	error  'org.codehaus.groovy.grails.web.pages', //  GSP
		   'org.codehaus.groovy.grails.web.sitemesh', //  layouts
		   'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
		   'org.codehaus.groovy.grails.web.mapping', // URL mapping
		   'org.codehaus.groovy.grails.commons', // core / classloading
		   'org.codehaus.groovy.grails.plugins', // plugins
		   'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
		   'org.springframework',
		   'org.hibernate',
		   'net.sf.ehcache.hibernate'

	warn   'org.mortbay.log'
}