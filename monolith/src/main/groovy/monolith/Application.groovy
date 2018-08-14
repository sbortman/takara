package monolith

import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic

import joms.oms.Init
import org.geotools.factory.Hints

@CompileStatic
class Application
{
	static void main( String[] args )
	{
		Hints.putSystemDefault( Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE )
		
		Init.instance().initialize()
		Micronaut.run( Application )
	}
}