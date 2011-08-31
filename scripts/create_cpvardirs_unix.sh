#!/bin/sh
# javadocs cannot use classpath variables in Eclipse. Provide workaround so that a team can share common .classpath and .project files.
# change the default location with grails create-eclipse-files --cpvars-dir=/some/other/dir
mkdir -p "/opt/prog/providers/groovy/grails/eclipse-cpvars"
cd "/opt/prog/providers/groovy/grails/eclipse-cpvars"
ln -s "/home/tes/.ivy2/cache" "GRAILS_IVYCACHE"
ln -s "/home/tes/.grails/1.3.4" "GRAILS_WORKDIR"
echo "Run grails create-eclipse-files again to create common paths to javadocs in .classpath (team members can share .classpath files and they can be in versioned in the source code repository)"
