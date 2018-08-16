package monolith.wfs

import io.micronaut.http.server.HttpServerConfiguration
import monolith.geoscript.GeoscriptService

import javax.inject.Inject
import javax.inject.Singleton

import groovy.xml.StreamingMarkupBuilder

@Singleton
class WebFeatureService
{
	@Inject
	HttpServerConfiguration serverConfiguration
	
	@Inject
	GeoscriptService geoscriptService
	
	
	def outputFormats = [
		'text/xml; subtype=gml/3.1.1',
		'GML2',
		'KML',
		'SHAPE-ZIP',
		'application/gml+xml; version=3.2',
		'application/json',
		'application/vnd.google-earth.kml xml',
		'application/vnd.google-earth.kml+xml',
		'csv',
		'gml3',
		'gml32',
		'json',
		'text/xml; subtype=gml/2.1.2',
		'text/xml; subtype=gml/3.2'
	]
	
	def geometryOperands = [
		'gml:Envelope',
		'gml:Point',
		'gml:LineString',
		'gml:Polygon'
	]
	
	def spatialOperators = [
		"Disjoint",
		"Equals",
		"DWithin",
		"Beyond",
		"Intersects",
		"Touches",
		"Crosses",
		"Within",
		"Contains",
		"Overlaps",
		"BBOX"
	]
	
	def comparisonOperators = [
		'LessThan',
		'GreaterThan',
		'LessThanEqualTo',
		'GreaterThanEqualTo',
		'EqualTo',
		'NotEqualTo',
		'Like',
		'Between',
		'NullCheck'
	]
	
	
	String getCapabilities()
	{
		def baseUrl = "http://${ serverConfiguration.host ?: 'localhost' }:${ serverConfiguration.port }"
		def wfsUrl = "${ baseUrl }/wfs"

//		def applicationContext = ApplicationContext.run()

//		applicationContext.allBeanDefinitions.each { println it }

//		println serverConfiguration.properties
		
		def content = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(
				xsi: "http://www.w3.org/2001/XMLSchema-instance",
				wfs: "http://www.opengis.net/wfs",
				ows: "http://www.opengis.net/ows",
				gml: "http://www.opengis.net/gml",
				ogc: "http://www.opengis.net/ogc",
				xlink: "http://www.w3.org/1999/xlink"
			)
			mkp.declareNamespace(geoscriptService.namespaces)
			wfs.WFS_Capabilities(
				xmlns: "http://www.opengis.net/wfs",
				version: "1.1.0",
				'xsi:schemaLocation': "http://www.opengis.net/wfs ${ baseUrl }/schemas/wfs/1.1.0/wfs.xsd",
				updateSequence: "165"
			) {
				ows.ServiceIdentification {
					ows.Title( 'GeoServer Web Feature Service' )
					ows.Abstract( 'This is the reference implementation of WFS 1.0.0 and WFS 1.1.0, supports all WFS operations including Transaction.' )
					ows.Keywords {
						def keywords = [
							'WFS',
							'WMS',
							'GEOSERVER'
						]
						keywords.each { keyword ->
							ows.Keyword( keyword )
						}
					}
					ows.ServiceType( 'WFS' )
					ows.ServiceTypeVersion( '1.1.0' )
					ows.Fees( 'NONE' )
					ows.AccessConstraints( 'NONE' )
				}
				ows.ServiceProvider {
					ows.ProviderName( 'The Ancient Geographers' )
					ows.ServiceContact {
						ows.IndividualName( 'Claudius Ptolomaeus' )
						ows.PositionName( 'Chief Geographer' )
						ows.ContactInfo {
							ows.Phone {
								ows.Voice()
								ows.Facsimile()
							}
							ows.Address {
								ows.DeliveryPoint()
								ows.City( 'Alexandria' )
								ows.AdministrativeArea()
								ows.PostalCode()
								ows.Country( 'Egypt' )
								ows.ElectronicMailAddress( 'claudius.ptolomaeus@gmail.com' )
							}
						}
					}
				}
				ows.OperationsMetadata {
					ows.Operation( name: "GetCapabilities" ) {
						ows.DCP {
							ows.HTTP {
								ows.Get( 'xlink:href': wfsUrl )
								ows.Post( 'xlink:href': wfsUrl )
							}
						}
						ows.Parameter( name: "AcceptVersions" ) {
							ows.Value( '1.0.0' )
							ows.Value( '1.1.0' )
						}
						ows.Parameter( name: "AcceptFormats" ) {
							ows.Value( 'text/xml' )
						}
					}
					ows.Operation( name: "DescribeFeatureType" ) {
						ows.DCP {
							ows.HTTP {
								ows.Get( 'xlink:href': wfsUrl )
								ows.Post( 'xlink:href': wfsUrl )
							}
						}
						ows.Parameter( name: "outputFormat" ) {
							ows.Value( 'text/xml; subtype=gml/3.1.1' )
						}
					}
					ows.Operation( name: "GetFeature" ) {
						ows.DCP {
							ows.HTTP {
								ows.Get( 'xlink:href': wfsUrl )
								ows.Post( 'xlink:href': wfsUrl )
							}
						}
						ows.Parameter( name: "resultType" ) {
							ows.Value( 'results' )
							ows.Value( 'hits' )
						}
						ows.Parameter( name: "outputFormat" ) {
							outputFormats.each { outputFormat ->
								ows.Value( outputFormat ) clear
							}
						}
						ows.Constraint( name: "LocalTraverseXLinkScope" ) {
							ows.Value( 2 )
						}
					}
					ows.Operation( name: "GetGmlObject" ) {
						ows.DCP {
							ows.HTTP {
								ows.Get( 'xlink:href': wfsUrl )
								ows.Post( 'xlink:href': wfsUrl )
							}
						}
					}
				}
				FeatureTypeList {
					Operations {
						Operation( 'Query' )
					}

					geoscriptService.features.each { feature ->
						
						FeatureType( "xmlns:${ feature.namespace.prefix }": feature.namespace.uri ) {
							Name( "${ feature.namespace.prefix }:${ feature.name }" )
							Title( feature.title )
							Abstract( feature.description )
							ows.Keywords {
								feature.keywords.each { keyword ->
									ows.Keyword( keyword )
								}
							}
							DefaultSRS( feature.proj )
							ows.WGS84BoundingBox {
								ows.LowerCorner( "${ feature.bounds.minX } ${ feature.bounds.minY }" )
								ows.UpperCorner( "${ feature.bounds.maxX } ${ feature.bounds.maxY }" )
							}
						}
					}
				}
				ogc.Filter_Capabilities {
					ogc.Spatial_Capabilities {
						ogc.GeometryOperands {
							geometryOperands.each { geometryOperand ->
								ogc.GeometryOperand( geometryOperand )
							}
						}
						ogc.SpatialOperators {
							spatialOperators.each { spatialOperator ->
								ogc.SpatialOperator( name: spatialOperator )
							}
						}
					}
					ogc.Scalar_Capabilities {
						ogc.LogicalOperators()
						ogc.ComparisonOperators {
							comparisonOperators.each { comparisonOperator ->
								ogc.ComparisonOperator( comparisonOperator )
							}
						}
						ogc.ArithmeticOperators {
							ogc.SimpleArithmetic()
							ogc.Functions {
								ogc.FunctionNames {
									geoscriptService.functions.each { function ->
										ogc.FunctionName( nArgs: function.nArgs, function.name )
									}
								}
							}
						}
					}
					ogc.Id_Capabilities {
						ogc.FID()
						ogc.EID()
					}
				}
			}
		}
		new StreamingMarkupBuilder( encoding: 'UTF-8' ).bind( content )
	}
	
	String describeFeatureType()
	{
		def baseUrl = "http://${ serverConfiguration.host ?: 'localhost' }:${ serverConfiguration.port }"
		
		def content = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(
				gml: "http://www.opengis.net/gml",
				xsd: "http://www.w3.org/2001/XMLSchema"
			)
			mkp.declareNamespace(
				omar: "http://omar.ossim.org"
			)
			
			xsd.schema(
				elementFormDefault: "qualified",
				targetNamespace: "http://omar.ossim.org"
			) {
				xsd.'import'( namespace: "http://www.opengis.net/gml", schemaLocation: "${ baseUrl }/schemas/gml/3.1.1/base/gml.xsd" )
				xsd.complexType( name: "raster_entryType" ) {
					xsd.complexContent {
						xsd.extension( base: "gml:AbstractFeatureType" ) {
							xsd.sequence {
								def fields = [
									[ maxOccurs: 1, minOccurs: 1, name: 'id', nillable: false, type: 'xsd:long' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'version', nillable: false, type: 'xsd:long' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'organization', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'other_tags_xml', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'sun_azimuth', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'sun_elevation', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'crosses_dateline', nillable: true, type: 'xsd:boolean' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'image_representation', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'azimuth_angle', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'number_of_bands', nillable: false, type: 'xsd:int' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'tie_point_set', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'country_code', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'index_id', nillable: false, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'niirs', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'exclude_policy', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'filename', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'ingest_date', nillable: true, type: 'xsd:dateTime' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'image_category', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'width', nillable: false, type: 'xsd:long' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'grazing_angle', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'gsd_unit', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'cloud_cover', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'security_code', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'security_classification', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'receive_date', nillable: true, type: 'xsd:dateTime' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'product_id', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'class_name', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'wac_code', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'file_type', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'ground_geom', nillable: false, type: 'gml:MultiSurfacePropertyType' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'keep_forever', nillable: true, type: 'xsd:boolean' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'title', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'data_type', nillable: false, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'release_id', nillable: true, type: 'xsd:decimal' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'be_number', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'sensor_id', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'style_id', nillable: true, type: 'xsd:decimal' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'gsdy', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'target_id', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'valid_model', nillable: true, type: 'xsd:int' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'height', nillable: false, type: 'xsd:long' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'bit_depth', nillable: false, type: 'xsd:int' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'entry_id', nillable: false, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'gsdx', nillable: true, type: 'xsd:double' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'isorce', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'number_of_res_levels', nillable: true, type: 'xsd:int' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'image_id', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'access_date', nillable: true, type: 'xsd:dateTime' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'acquisition_date', nillable: true, type: 'xsd:dateTime' ],
									[ maxOccurs: 1, minOccurs: 0, name: 'mission_id', nillable: true, type: 'xsd:string' ],
									[ maxOccurs: 1, minOccurs: 1, name: 'raster_data_set_id', nillable: false, type: 'xsd:long' ]
								]
								fields.each { field ->
									xsd.element( maxOccurs: field.maxOccurs, minOccurs: field.minOccurs, name: field.name, nillable: field.nillable, type: field.type )
								}
							}
						}
					}
				}
				xsd.element( name: "raster_entry", substitutionGroup: "gml:_Feature", type: "omar:raster_entryType" )
			}
		}
		new StreamingMarkupBuilder( encoding: 'UTF-8' ).bind( content )
	}
	
	String getFeature()
	{
		"""
<?xml version="1.0" encoding="UTF-8"?>
<wfs:FeatureCollection xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:omar="http://omar.ossim.org" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" numberOfFeatures="10" timeStamp="2018-08-14T17:40:04.184Z" xsi:schemaLocation="${
			wfsUrl
		}?service=WFS&amp;version=1.1.0&amp;request=DescribeFeatureType&amp;typeName=omar%3Araster_entry http://www.opengis.net/wfs ${
			baseUrl
		}/schemas/wfs/1.1.0/wfs.xsd">
  <gml:featureMembers>
    <omar:raster_entry gml:id="raster_entry.1">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>1</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>dc917eb1f0bd211f73bbe2a0bfdaf40a0e7c479820f596ef3efbb7adf3e46617</omar:index_id>
      <omar:filename>/2015/05/14/07/56/CHEVY/IMPALA/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/5731205742738394357224.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:49.626Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-72.28489872 4.21856274 -70.56801935 3.19273369 -69.5421903 4.90961306 -71.25906967 5.93544211 -72.28489872 4.21856274</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>IMPALA</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2015-05-14T11:56:53.267Z</omar:acquisition_date>
      <omar:mission_id>CHEVY</omar:mission_id>
      <omar:raster_data_set_id>1</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.2">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>2</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>5f6e1304db8d836439805a95ef330d57943ff7dddc082814a2e630f60c581af5</omar:index_id>
      <omar:filename>/2013/05/18/01/32/HONDA/ACCORD/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/2771657191486724647953.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:50.021Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-18.90759306 -139.76766961 -17.06159253 -138.99807099 -17.83119115 -137.15207046 -19.67719169 -137.92166908 -18.90759306 -139.76766961</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>ACCORD</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2013-05-18T05:32:56.016Z</omar:acquisition_date>
      <omar:mission_id>HONDA</omar:mission_id>
      <omar:raster_data_set_id>2</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.3">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>3</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>67822328d3b54de12f64b17d1c55ef923d6b8013145ed62f35264c103ba58c08</omar:index_id>
      <omar:filename>/2018/08/04/15/27/TOYOTA/4-RUNNER/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/2558674047211837757892.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:50.061Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-71.29803252 -100.35053613 -69.82208591 -99.00087632 -71.17174571 -97.52492971 -72.64769233 -98.87458952 -71.29803252 -100.35053613</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>4-RUNNER</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2018-08-04T19:27:31.054Z</omar:acquisition_date>
      <omar:mission_id>TOYOTA</omar:mission_id>
      <omar:raster_data_set_id>3</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.4">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>4</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>eb6f7580484bfe7e1b4655aae0214b2cd13fda7efa33578ab06d21477daacaa5</omar:index_id>
      <omar:filename>/2016/11/20/23/53/PONTIAC/SUN-FIRE/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/760243259423299961401.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:50.093Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-84.05335183 -108.19671874 -86.01934855 -107.82949006 -86.38657723 -109.79548677 -84.42058052 -110.16271545 -84.05335183 -108.19671874</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>SUN-FIRE</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2016-11-21T04:53:25.087Z</omar:acquisition_date>
      <omar:mission_id>PONTIAC</omar:mission_id>
      <omar:raster_data_set_id>4</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.2671">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>2671</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>d1c63a6ef4ac6a0880c99666f3acaed2be2e31aa4c33b53feb04fa2be8884032</omar:index_id>
      <omar:filename>/2011/09/10/09/31/CHEVY/IMPALA/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/0501408641239451588868.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:43:48.771Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-36.98227179 31.92207893 -35.86922033 33.58373968 -37.53088108 34.69679114 -38.64393254 33.03513039 -36.98227179 31.92207893</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>IMPALA</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2011-09-10T13:31:21.769Z</omar:acquisition_date>
      <omar:mission_id>CHEVY</omar:mission_id>
      <omar:raster_data_set_id>2671</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.2672">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>2672</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>9565912f67d1c5cfe4eab84fd191d0bd9abc80f5c4734356c124674dece60c0f</omar:index_id>
      <omar:filename>/2014/08/04/01/54/HONDA/ACCORD/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/4627600790115574937117.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:43:48.783Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>19.44618998 6.84941899 21.03412235 8.06534283 19.8181985 9.6532752 18.23026613 8.43735136 19.44618998 6.84941899</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>ACCORD</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2014-08-04T05:54:42.782Z</omar:acquisition_date>
      <omar:mission_id>HONDA</omar:mission_id>
      <omar:raster_data_set_id>2672</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.6157">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>6157</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>f0ba72c6240bf634396bceddb6bee592a47771fced8ad167b1a20ee41e773130</omar:index_id>
      <omar:filename>/2016/11/12/03/11/CHEVY/VOLT/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/5767209627279096709596.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:44:49.064Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-0.75414667 105.33899128 1.18928818 105.81128457 0.71699489 107.75471943 -1.22643997 107.28242613 -0.75414667 105.33899128</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>VOLT</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2016-11-12T08:11:58.061Z</omar:acquisition_date>
      <omar:mission_id>CHEVY</omar:mission_id>
      <omar:raster_data_set_id>6157</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.5">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>5</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>f4f10113cd833c1e88666940cafc73d8999a74e8c41b28e403dd611f9a16d60c</omar:index_id>
      <omar:filename>/2011/04/30/16/17/PONTIAC/TRANS-AM/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/5322388812939553873812.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:50.128Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-80.01240585 106.53979199 -81.92759468 105.96355268 -81.35135537 104.04836385 -79.43616654 104.62460316 -80.01240585 106.53979199</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>TRANS-AM</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2011-04-30T20:17:45.124Z</omar:acquisition_date>
      <omar:mission_id>PONTIAC</omar:mission_id>
      <omar:raster_data_set_id>5</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.6">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>6</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>77515f50528866a7f8470ebf765712ed7f55f36b8ccdf0809e7352e2648fc7bf</omar:index_id>
      <omar:filename>/2010/09/05/12/53/HONDA/CR-X/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/0408819616733559928373.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:50.161Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>-47.3461669 -29.50078396 -45.52049935 -28.68412125 -46.33716206 -26.85845369 -48.16282961 -27.6751164 -47.3461669 -29.50078396</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>CR-X</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2010-09-05T16:53:42.155Z</omar:acquisition_date>
      <omar:mission_id>HONDA</omar:mission_id>
      <omar:raster_data_set_id>6</omar:raster_data_set_id>
    </omar:raster_entry>
    <omar:raster_entry gml:id="raster_entry.7">
      <gml:description>ORBVIEW3</gml:description>
      <omar:id>7</omar:id>
      <omar:version>1</omar:version>
      <omar:organization>000ORBIMAGE</omar:organization>
      <omar:sun_azimuth>112.4</omar:sun_azimuth>
      <omar:sun_elevation>69.3</omar:sun_elevation>
      <omar:crosses_dateline>false</omar:crosses_dateline>
      <omar:image_representation>MONO</omar:image_representation>
      <omar:azimuth_angle>41.7</omar:azimuth_angle>
      <omar:number_of_bands>1</omar:number_of_bands>
      <omar:tie_point_set>&lt;TiePointSet&gt;&lt;Image&gt;&lt;coordinates&gt;0,0 8015,0 8015,28671 0,28671&lt;/coordinates&gt;&lt;/Image&gt;&lt;Ground&gt;&lt;coordinates&gt;-80.5948122142511,28.7028439492629 -80.5325639801344,28.6541927163518 -80.7263809338534,28.4629059947826 -80.7890898156038,28.5115269575306&lt;/coordinates&gt;&lt;/Ground&gt;&lt;/TiePointSet&gt;</omar:tie_point_set>
      <omar:country_code>US</omar:country_code>
      <omar:index_id>75400d741e5ffea2423dbf61cd06f06a29d813c8d651996c93dfe30306173ecb</omar:index_id>
      <omar:filename>/2018/06/05/05/09/PONTIAC/FIREBIRD/var/folders/yj/0qn_z4tx4x39q18c7xhl16sc0000gq/T/1534601815694848853945.ntf</omar:filename>
      <omar:ingest_date>2018-06-27T00:41:50.193Z</omar:ingest_date>
      <omar:image_category>VIS</omar:image_category>
      <omar:width>8016</omar:width>
      <omar:grazing_angle>80.13</omar:grazing_angle>
      <omar:gsd_unit>meters</omar:gsd_unit>
      <omar:cloud_cover>10.0</omar:cloud_cover>
      <omar:security_classification>UNCLASSIFIED</omar:security_classification>
      <omar:class_name>ossimNitfTileSource</omar:class_name>
      <omar:wac_code>0466</omar:wac_code>
      <omar:file_type>nitf</omar:file_type>
      <omar:ground_geom>
        <gml:MultiSurface srsName="http://www.opengis.net/gml/srs/epsg.xml#4326" srsDimension="2">
          <gml:surfaceMember>
            <gml:Polygon>
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>32.85940569 157.64673538 30.97348069 156.98093745 31.63927862 155.09501245 33.52520362 155.76081038 32.85940569 157.64673538</gml:posList>
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </gml:surfaceMember>
        </gml:MultiSurface>
      </omar:ground_geom>
      <omar:title>26JUL05OV03000003V050726P0000820271A0100007003410_00574200</omar:title>
      <omar:data_type>uint</omar:data_type>
      <omar:sensor_id>FIREBIRD</omar:sensor_id>
      <omar:gsdy>0.995015206188554</omar:gsdy>
      <omar:valid_model>1</omar:valid_model>
      <omar:height>28672</omar:height>
      <omar:bit_depth>8</omar:bit_depth>
      <omar:entry_id>0</omar:entry_id>
      <omar:gsdx>0.999937922715187</omar:gsdx>
      <omar:isorce>ORBVIEW3</omar:isorce>
      <omar:number_of_res_levels>8</omar:number_of_res_levels>
      <omar:image_id>BS002749</omar:image_id>
      <omar:acquisition_date>2018-06-05T09:09:15.188Z</omar:acquisition_date>
      <omar:mission_id>PONTIAC</omar:mission_id>
      <omar:raster_data_set_id>7</omar:raster_data_set_id>
    </omar:raster_entry>
  </gml:featureMembers>
</wfs:FeatureCollection>
""".trim()
	}
}