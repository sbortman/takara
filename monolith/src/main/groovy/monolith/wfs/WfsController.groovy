package monolith.wfs

import io.micronaut.http.HttpParameters
import io.micronaut.http.HttpRequest
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
	HttpResponse index( HttpRequest request )
	{
		HttpParameters params = request.parameters
		
		println params
		
		def wfsRequest = params.find { it.key.toUpperCase() == 'REQUEST' }?.value?.first()?.toUpperCase()
		HttpResponse results
		
		println wfsRequest
		
		switch ( wfsRequest )
		{
		case 'GETCAPABILITIES':
			results = HttpResponse.ok( webFeatureService.getCapabilities() ).contentType( MediaType.TEXT_XML_TYPE )
			break
		case 'DESCRIBEFEATURETYPE':
			results = HttpResponse.ok( webFeatureService.describeFeatureType() ).contentType( MediaType.TEXT_XML_TYPE )
			break
		default:
			results = HttpStatus.OK
		}
		return results
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