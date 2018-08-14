package monolith.wfs

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpResponse

import javax.inject.Inject


@Controller( "/wfs" )
class WfsController
{
	
	@Inject
	WebFeatureService webFeatureService
	
	@Get( "/" )
	HttpStatus index()
	{
		return HttpStatus.OK
	}
	
	@Get( "/getCapabilities" )
	HttpResponse<String> getCapabilities()
	{
		return HttpResponse.ok( webFeatureService.getCapabilities() ).contentType( MediaType.TEXT_XML_TYPE )
	}
	
	@Get( "/describeFeatureType" )
	HttpResponse<String> describeFeatureType()
	{
		return HttpResponse.ok( webFeatureService.describeFeatureType() ).contentType( MediaType.TEXT_XML_TYPE )
	}
	
	@Get( "/getFeature" )
	HttpResponse<String> getFeature()
	{
		return HttpResponse.ok( webFeatureService.getFeature() ).contentType( MediaType.TEXT_XML_TYPE )
	}
}