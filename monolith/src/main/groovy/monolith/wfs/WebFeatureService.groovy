package monolith.wfs

import javax.inject.Singleton

import groovy.xml.StreamingMarkupBuilder

@Singleton
class WebFeatureService {
    String getCapabilities() {
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
            mkp.declareNamespace(
                    cite: "http://www.opengeospatial.net/cite",
                    tiger: "http://www.census.gov",
                    nurc: "http://www.nurc.nato.int",
                    sde: "http://geoserver.sf.net",
                    'it.geosolutions': "http://www.geo-solutions.it",
                    topp: "http://www.openplans.org/topp",
                    sf: "http://www.openplans.org/spearfish",
                    omar: "http://omar.ossim.org",
                    streetview: "http://streetview.ossim.io"
            )
            wfs.WFS_Capabilities(
                    xmlns: "http://www.opengis.net/wfs",
                    version: "1.1.0",
                    'xsi:schemaLocation': "http://www.opengis.net/wfs http://localhost:8080/geoserver/schemas/wfs/1.1.0/wfs.xsd",
                    updateSequence: "165"
            ) {
                ows.ServiceIdentification {
                    ows.Title('GeoServer Web Feature Service')
                    ows.Abstract('This is the reference implementation of WFS 1.0.0 and WFS 1.1.0, supports all WFS operations including Transaction.')
                    ows.Keywords {
                        def keywords = [
                                'WFS',
                                'WMS',
                                'GEOSERVER'
                        ]
                        keywords.each { keyword ->
                            ows.Keyword(keyword)
                        }
                    }
                    ows.ServiceType('WFS')
                    ows.ServiceTypeVersion('1.1.0')
                    ows.Fees('NONE')
                    ows.AccessConstraints('NONE')
                }
                ows.ServiceProvider {
                    ows.ProviderName('The Ancient Geographers')
                    ows.ServiceContact {
                        ows.IndividualName('Claudius Ptolomaeus')
                        ows.PositionName('Chief Geographer')
                        ows.ContactInfo {
                            ows.Phone {
                                ows.Voice()
                                ows.Facsimile()
                            }
                            ows.Address {
                                ows.DeliveryPoint()
                                ows.City('Alexandria')
                                ows.AdministrativeArea()
                                ows.PostalCode()
                                ows.Country('Egypt')
                                ows.ElectronicMailAddress('claudius.ptolomaeus@gmail.com')
                            }
                        }
                    }
                }
                ows.OperationsMetadata {
                    ows.Operation(name: "GetCapabilities") {
                        ows.DCP {
                            ows.HTTP {
                                ows.Get('xlink:href': "http://localhost:8080/geoserver/wfs")
                                ows.Post('xlink:href': "http://localhost:8080/geoserver/wfs")
                            }
                        }
                        ows.Parameter(name: "AcceptVersions") {
                            ows.Value('1.0.0')
                            ows.Value('1.1.0')
                        }
                        ows.Parameter(name: "AcceptFormats") {
                            ows.Value('text/xml')
                        }
                    }
                    ows.Operation(name: "DescribeFeatureType") {
                        ows.DCP {
                            ows.HTTP {
                                ows.Get('xlink:href': "http://localhost:8080/geoserver/wfs")
                                ows.Post('xlink:href': "http://localhost:8080/geoserver/wfs")
                            }
                        }
                        ows.Parameter(name: "outputFormat") {
                            ows.Value('text/xml; subtype=gml/3.1.1')
                        }
                    }
                    ows.Operation(name: "GetFeature") {
                        ows.DCP {
                            ows.HTTP {
                                ows.Get('xlink:href': "http://localhost:8080/geoserver/wfs")
                                ows.Post('xlink:href': "http://localhost:8080/geoserver/wfs")
                            }
                        }
                        ows.Parameter(name: "resultType") {
                            ows.Value('results')
                            ows.Value('hits')
                        }
                        ows.Parameter(name: "outputFormat") {
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
                            outputFormats.each { outputFormat ->
                                ows.Value(outputFormat) clear
                            }
                        }
                        ows.Constraint(name: "LocalTraverseXLinkScope") {
                            ows.Value(2)
                        }
                    }
                    ows.Operation(name: "GetGmlObject") {
                        ows.DCP {
                            ows.HTTP {
                                ows.Get('xlink:href': "http://localhost:8080/geoserver/wfs")
                                ows.Post('xlink:href': "http://localhost:8080/geoserver/wfs")
                            }
                        }
                    }
                }
                FeatureTypeList {
                    Operations {
                        Operation('Query')
                    }
                    def features = [[
                                            namespace  : [
                                                    prefix: 'tiger',
                                                    uri   : "http://www.census.gov"
                                            ],
                                            name       : 'poly_landmarks',
                                            title      : 'Manhattan (NY) landmarks',
                                            description: 'Manhattan landmarks, identifies water, lakes, parks, interesting buildilngs',
                                            keywords   : [
                                                    'landmarks',
                                                    'DS_poly_landmarks',
                                                    'manhattan',
                                                    'poly_landmarks'
                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#4326',
                                            bounds     : [
                                                    minX: -74.047185,
                                                    minY: 40.679648,
                                                    maxX: -73.90782,
                                                    maxY: 40.882078
                                            ]
                                    ], [
                                            namespace  : [
                                                    prefix: 'tiger',
                                                    uri   : "http://www.census.gov"
                                            ],
                                            name       : 'poi',
                                            title      : 'Manhattan (NY) points of interest',
                                            description: 'Points of interest in New York, New York (on Manhattan). One of the attributes contains the name of a file with a picture of the point of interest.',
                                            keywords   : [
                                                    'poi',
                                                    'Manhattan',
                                                    'DS_poi',
                                                    'points_of_interest'
                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#4326',
                                            bounds     : [
                                                    minX: -74.0118315772888,
                                                    minY: 40.70754683896324,
                                                    maxX: -74.00857344353275,
                                                    maxY: 40.711945649065406
                                            ]
                                    ], [
                                            namespace  : [
                                                    prefix: 'tiger',
                                                    uri   : "http://www.census.gov"
                                            ],
                                            name       : 'tiger_roads',
                                            title      : 'Manhattan (NY) roads',
                                            description: 'Highly simplified road layout of Manhattan in New York..',
                                            keywords   : [
                                                    'DS_tiger_roads',
                                                    'tiger_roads',
                                                    'roads'
                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#4326',
                                            bounds     : [
                                                    minX: -74.02722,
                                                    minY: 40.684221,
                                                    maxX: -73.907005,
                                                    maxY: 40.878178
                                            ]

                                    ], [
                                            namespace  : [
                                                    prefix: 'sf',
                                                    uri   : "http://www.openplans.org/spearfish"
                                            ],
                                            name       : 'archsites',
                                            title      : 'Spearfish archeological sites',
                                            description: 'Sample data from GRASS, archeological sites location, Spearfish, South Dakota, USA',
                                            keywords   : [
                                                    'archsites',
                                                    'spearfish',
                                                    'sfArchsites',
                                                    'archeology'

                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#26713',
                                            bounds     : [
                                                    minX: -103.8725637911543,
                                                    minY: 44.37740330855979,
                                                    maxX: -103.63794182141925,
                                                    maxY: 44.48804280772808
                                            ]
                                    ], [
                                            namespace  : [
                                                    prefix: 'sf',
                                                    uri   : "http://www.openplans.org/spearfish"
                                            ],
                                            name       : 'bugsites',
                                            title      : 'Spearfish bug locations',
                                            description: 'Sample data from GRASS, bug sites location, Spearfish, South Dakota, USA',
                                            keywords   : [
                                                    'spearfish',
                                                    'sfBugsites',
                                                    'insects',
                                                    'bugsites',
                                                    'tiger_beetles'

                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#26713',
                                            bounds     : [
                                                    minX: -103.86796131703647,
                                                    minY: 44.373938816704396,
                                                    maxX: -103.63773523234195,
                                                    maxY: 44.43418821380063
                                            ]
                    ], [
                                            namespace  : [
                                                    prefix: 'sf',
                                                    uri   : "http://www.openplans.org/spearfish"
                                            ],
                                            name       : 'restricted',
                                            title      : 'Spearfish restricted areas',
                                            description: 'Sample data from GRASS, restricted areas, Spearfish, South Dakota, USA',
                                            keywords   : [
                                                    'spearfish',
                                                    'restricted',
                                                    'areas',
                                                    'sfRestricted'
                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#26713',
                                            bounds     : [
                                                    minX: -103.85057172920756,
                                                    minY: 44.39436387625042,
                                                    maxX: -103.74741494853805,
                                                    maxY: 44.48215752041131
                                            ]
                    ], [
                                            namespace  : [
                                                    prefix: 'sf',
                                                    uri   : "http://www.openplans.org/spearfish"
                                            ],
                                            name       : 'roads',
                                            title      : 'Spearfish roads',
                                            description: 'Sample data from GRASS, road layout, Spearfish, South Dakota, USA',
                                            keywords   : [
                                                    'sfRoads',
                                                    'spearfish',
                                                    'roads'
                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#26713',
                                            bounds     : [
                                                    minX: -103.87741691493184,
                                                    minY: 44.37087275281798,
                                                    maxX: -103.62231404880659,
                                                    maxY: 44.50015918338962
                                            ]
                    ], [
                                            namespace  : [
                                                    prefix: 'sf',
                                                    uri   : "http://www.openplans.org/spearfish"
                                            ],
                                            name       : 'streams',
                                            title      : 'Spearfish streams',
                                            description: 'Sample data from GRASS, streams, Spearfish, South Dakota, USA',
                                            keywords   : [
                                                    'spearfish',
                                                    'sfStreams',
                                                    'streams'
                                            ],
                                            proj       : 'http://www.opengis.net/gml/srs/epsg.xml#26713',
                                            bounds     : [
                                                    minX: -103.87789019829768,
                                                    minY: 44.372335260095554,
                                                    maxX: -103.62287788915457,
                                                    maxY: 44.502218486214815
                                            ]
                                    ]]
                    features.each { feature ->

                        FeatureType("xmlns:${feature.namespace.prefix}": feature.namespace.uri) {
                            Name("${feature.namespace.prefix}:${feature.name}")
                            Title(feature.title)
                            Abstract(feature.description)
                            ows.Keywords {
                                feature.keywords.each { keyword ->
                                    ows.Keyword(keyword)
                                }
                            }
                            DefaultSRS(feature.proj)
                            ows.WGS84BoundingBox {
                                ows.LowerCorner("${feature.bounds.minX} ${feature.bounds.minY}")
                                ows.UpperCorner("${feature.bounds.maxX} ${feature.bounds.maxY}")
                            }
                        }
                    }
/*
    <FeatureType xmlns:topp="http://www.openplans.org/topp">
      <Name>topp:tasmania_cities</Name>
      <Title>Tasmania cities</Title>
      <Abstract>Cities in Tasmania (actually, just the capital)</Abstract>
      <ows:Keywords>
        <ows:Keyword>cities</ows:Keyword>
        <ows:Keyword>Tasmania</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>145.19754 -43.423512</ows:LowerCorner>
        <ows:UpperCorner>148.27298000000002 -40.852802</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:topp="http://www.openplans.org/topp">
      <Name>topp:tasmania_roads</Name>
      <Title>Tasmania roads</Title>
      <Abstract>Main Tasmania roads</Abstract>
      <ows:Keywords>
        <ows:Keyword>Roads</ows:Keyword>
        <ows:Keyword>Tasmania</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>145.19754 -43.423512</ows:LowerCorner>
        <ows:UpperCorner>148.27298000000002 -40.852802</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:topp="http://www.openplans.org/topp">
      <Name>topp:tasmania_state_boundaries</Name>
      <Title>Tasmania state boundaries</Title>
      <Abstract>Tasmania state boundaries</Abstract>
      <ows:Keywords>
        <ows:Keyword>boundaries</ows:Keyword>
        <ows:Keyword>tasmania_state_boundaries</ows:Keyword>
        <ows:Keyword>Tasmania</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>143.83482400000003 -43.648056</ows:LowerCorner>
        <ows:UpperCorner>148.47914100000003 -39.573891</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:topp="http://www.openplans.org/topp">
      <Name>topp:tasmania_water_bodies</Name>
      <Title>Tasmania water bodies</Title>
      <Abstract>Tasmania water bodies</Abstract>
      <ows:Keywords>
        <ows:Keyword>Lakes</ows:Keyword>
        <ows:Keyword>Bodies</ows:Keyword>
        <ows:Keyword>Australia</ows:Keyword>
        <ows:Keyword>Water</ows:Keyword>
        <ows:Keyword>Tasmania</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>145.97161899999998 -43.031944</ows:LowerCorner>
        <ows:UpperCorner>147.219696 -41.775558</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:topp="http://www.openplans.org/topp">
      <Name>topp:states</Name>
      <Title>USA Population</Title>
      <Abstract>This is some census data on the states.</Abstract>
      <ows:Keywords>
        <ows:Keyword>census</ows:Keyword>
        <ows:Keyword>united</ows:Keyword>
        <ows:Keyword>boundaries</ows:Keyword>
        <ows:Keyword>state</ows:Keyword>
        <ows:Keyword>states</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>-124.731422 24.955967</ows:LowerCorner>
        <ows:UpperCorner>-66.969849 49.371735</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:tiger="http://www.census.gov">
      <Name>tiger:giant_polygon</Name>
      <Title>World rectangle</Title>
      <Abstract>A simple rectangular polygon covering most of the world, it's only used for the purpose of providing a background (WMS bgcolor could be used instead)</Abstract>
      <ows:Keywords>
        <ows:Keyword>DS_giant_polygon</ows:Keyword>
        <ows:Keyword>giant_polygon</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>-180.0 -90.0</ows:LowerCorner>
        <ows:UpperCorner>180.0 90.0</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:omar="http://omar.ossim.org">
      <Name>omar:raster_entry</Name>
      <Title>raster_entry</Title>
      <Abstract/>
      <ows:Keywords>
        <ows:Keyword>features</ows:Keyword>
        <ows:Keyword>raster_entry</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>-183.20295715332 -92.3009185791016</ows:LowerCorner>
        <ows:UpperCorner>183.197738647461 92.3120803833008</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
    <FeatureType xmlns:streetview="http://streetview.ossim.io">
      <Name>streetview:road_streetview_subset</Name>
      <Title>road_streetview_subset</Title>
      <Abstract/>
      <ows:Keywords>
        <ows:Keyword>features</ows:Keyword>
        <ows:Keyword>road_streetview_subset</ows:Keyword>
      </ows:Keywords>
      <DefaultSRS>http://www.opengis.net/gml/srs/epsg.xml#4326</DefaultSRS>
      <ows:WGS84BoundingBox>
        <ows:LowerCorner>116.368103027344 39.9055595397949</ows:LowerCorner>
        <ows:UpperCorner>116.396774291992 39.9064750671387</ows:UpperCorner>
      </ows:WGS84BoundingBox>
    </FeatureType>
*/
                }
                ogc.Filter_Capabilities {
                    ogc.Spatial_Capabilities {
                        ogc.GeometryOperands {
                            def geometryOperands = [
                                    'gml:Envelope',
                                    'gml:Point',
                                    'gml:LineString',
                                    'gml:Polygon'
                            ]
                            geometryOperands.each { geometryOperand ->
                                ogc.GeometryOperand(geometryOperand)
                            }
                        }
                        ogc.SpatialOperators {
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
                            spatialOperators.each { spatialOperator ->
                                ogc.SpatialOperator(name: spatialOperator)
                            }
                        }
                    }
                    ogc.Scalar_Capabilities {
                        ogc.LogicalOperators()
                        ogc.ComparisonOperators {
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
                            comparisonOperators.each { comparisonOperator ->
                                ogc.ComparisonOperator(comparisonOperator)
                            }
                        }
                        ogc.ArithmeticOperators {
                            ogc.SimpleArithmetic()
                            ogc.Functions {
                                ogc.FunctionNames {
                                    def functions = [
                                            [name: 'abs', nArgs: 1],
                                            [name: 'abs_2', nArgs: 1],
                                            [name: 'abs_3', nArgs: 1],
                                            [name: 'abs_4', nArgs: 1],
                                            [name: 'acos', nArgs: 1],
                                            [name: 'AddCoverages', nArgs: 2],
                                            [name: 'Affine', nArgs: -1],
                                            [name: 'Aggregate', nArgs: -2],
                                            [name: 'Area', nArgs: 1],
                                            [name: 'area2', nArgs: 1],
                                            [name: 'AreaGrid', nArgs: 3],
                                            [name: 'asin', nArgs: 1],
                                            [name: 'atan', nArgs: 1],
                                            [name: 'atan2', nArgs: 2],
                                            [name: 'attributeCount', nArgs: 1],
                                            [name: 'BandMerge', nArgs: -1],
                                            [name: 'BandSelect', nArgs: -2],
                                            [name: 'BarnesSurface', nArgs: -6],
                                            [name: 'between', nArgs: 3],
                                            [name: 'boundary', nArgs: 1],
                                            [name: 'boundaryDimension', nArgs: 1],
                                            [name: 'Bounds', nArgs: 1],
                                            [name: 'buffer', nArgs: 2],
                                            [name: 'BufferFeatureCollection', nArgs: -2],
                                            [name: 'bufferWithSegments', nArgs: 3],
                                            [name: 'Categorize', nArgs: 7],
                                            [name: 'ceil', nArgs: 1],
                                            [name: 'centroid', nArgs: 1],
                                            [name: 'classify', nArgs: 2],
                                            [name: 'Clip', nArgs: -2],
                                            [name: 'CollectGeometries', nArgs: 1],
                                            [name: 'Collection_Average', nArgs: 1],
                                            [name: 'Collection_Bounds', nArgs: 1],
                                            [name: 'Collection_Count', nArgs: 0],
                                            [name: 'Collection_Max', nArgs: 1],
                                            [name: 'Collection_Median', nArgs: 1],
                                            [name: 'Collection_Min', nArgs: 1],
                                            [name: 'Collection_Nearest', nArgs: 1],
                                            [name: 'Collection_Sum', nArgs: 1],
                                            [name: 'Collection_Unique', nArgs: 1],
                                            [name: 'Concatenate', nArgs: -2],
                                            [name: 'contains', nArgs: 2],
                                            [name: 'Contour', nArgs: -1],
                                            [name: 'contrast', nArgs: -1],
                                            [name: 'convert', nArgs: 2],
                                            [name: 'convexHull', nArgs: 1],
                                            [name: 'ConvolveCoverage', nArgs: -1],
                                            [name: 'cos', nArgs: 1],
                                            [name: 'Count', nArgs: 1],
                                            [name: 'CoverageClassStats', nArgs: -1],
                                            [name: 'CropCoverage', nArgs: 2],
                                            [name: 'crosses', nArgs: 2],
                                            [name: 'darken', nArgs: -2],
                                            [name: 'dateFormat', nArgs: 2],
                                            [name: 'dateParse', nArgs: 2],
                                            [name: 'desaturate', nArgs: -2],
                                            [name: 'difference', nArgs: 2],
                                            [name: 'dimension', nArgs: 1],
                                            [name: 'disjoint', nArgs: 2],
                                            [name: 'disjoint3D', nArgs: 2],
                                            [name: 'distance', nArgs: 2],
                                            [name: 'distance3D', nArgs: 2],
                                            [name: 'double2bool', nArgs: 1],
                                            [name: 'endAngle', nArgs: 1],
                                            [name: 'endPoint', nArgs: 1],
                                            [name: 'env', nArgs: 1],
                                            [name: 'envelope', nArgs: 1],
                                            [name: 'EqualInterval', nArgs: 2],
                                            [name: 'equalsExact', nArgs: 2],
                                            [name: 'equalsExactTolerance', nArgs: 3],
                                            [name: 'equalTo', nArgs: 2],
                                            [name: 'exp', nArgs: 1],
                                            [name: 'exteriorRing', nArgs: 1],
                                            [name: 'Feature', nArgs: 3],
                                            [name: 'FeatureClassStats', nArgs: -2],
                                            [name: 'floor', nArgs: 1],
                                            [name: 'geometry', nArgs: 0],
                                            [name: 'geometryType', nArgs: 1],
                                            [name: 'geomFromWKT', nArgs: 1],
                                            [name: 'geomLength', nArgs: 1],
                                            [name: 'getGeometryN', nArgs: 2],
                                            [name: 'getX', nArgs: 1],
                                            [name: 'getY', nArgs: 1],
                                            [name: 'getz', nArgs: 1],
                                            [name: 'grayscale', nArgs: 1],
                                            [name: 'greaterEqualThan', nArgs: 2],
                                            [name: 'greaterThan', nArgs: 2],
                                            [name: 'Grid', nArgs: -3],
                                            [name: 'Heatmap', nArgs: -5],
                                            [name: 'hsl', nArgs: 3],
                                            [name: 'id', nArgs: 0],
                                            [name: 'IEEEremainder', nArgs: 2],
                                            [name: 'if_then_else', nArgs: 3],
                                            [name: 'in', nArgs: -2],
                                            [name: 'in10', nArgs: 11],
                                            [name: 'in2', nArgs: 3],
                                            [name: 'in3', nArgs: 4],
                                            [name: 'in4', nArgs: 5],
                                            [name: 'in5', nArgs: 6],
                                            [name: 'in6', nArgs: 7],
                                            [name: 'in7', nArgs: 8],
                                            [name: 'in8', nArgs: 9],
                                            [name: 'in9', nArgs: 10],
                                            [name: 'InclusionFeatureCollection', nArgs: 2],
                                            [name: 'int2bbool', nArgs: 1],
                                            [name: 'int2ddouble', nArgs: 1],
                                            [name: 'interiorPoint', nArgs: 1],
                                            [name: 'interiorRingN', nArgs: 2],
                                            [name: 'Interpolate', nArgs: -5],
                                            [name: 'intersection', nArgs: 2],
                                            [name: 'IntersectionFeatureCollection', nArgs: -2],
                                            [name: 'intersects', nArgs: 2],
                                            [name: 'intersects3D', nArgs: 2],
                                            [name: 'isClosed', nArgs: 1],
                                            [name: 'isCoverage', nArgs: 0],
                                            [name: 'isEmpty', nArgs: 1],
                                            [name: 'isInstanceOf', nArgs: 1],
                                            [name: 'isLike', nArgs: 2],
                                            [name: 'isNull', nArgs: 1],
                                            [name: 'isometric', nArgs: 2],
                                            [name: 'isRing', nArgs: 1],
                                            [name: 'isSimple', nArgs: 1],
                                            [name: 'isValid', nArgs: 1],
                                            [name: 'isWithinDistance', nArgs: 3],
                                            [name: 'isWithinDistance3D', nArgs: 3],
                                            [name: 'Jenks', nArgs: 2],
                                            [name: 'length', nArgs: 1],
                                            [name: 'lessEqualThan', nArgs: 2],
                                            [name: 'lessThan', nArgs: 2],
                                            [name: 'lighten', nArgs: -2],
                                            [name: 'list', nArgs: -1],
                                            [name: 'listMultiply', nArgs: 2],
                                            [name: 'log', nArgs: 1],
                                            [name: 'LRSGeocode', nArgs: 4],
                                            [name: 'LRSMeasure', nArgs: -4],
                                            [name: 'LRSSegment', nArgs: 5],
                                            [name: 'max', nArgs: 2],
                                            [name: 'max_2', nArgs: 2],
                                            [name: 'max_3', nArgs: 2],
                                            [name: 'max_4', nArgs: 2],
                                            [name: 'min', nArgs: 2],
                                            [name: 'min_2', nArgs: 2],
                                            [name: 'min_3', nArgs: 2],
                                            [name: 'min_4', nArgs: 2],
                                            [name: 'mincircle', nArgs: 1],
                                            [name: 'minimumdiameter', nArgs: 1],
                                            [name: 'minrectangle', nArgs: 1],
                                            [name: 'mix', nArgs: 3],
                                            [name: 'modulo', nArgs: 2],
                                            [name: 'MultiplyCoverages', nArgs: 2],
                                            [name: 'Nearest', nArgs: -2],
                                            [name: 'NormalizeCoverage', nArgs: 1],
                                            [name: 'not', nArgs: 1],
                                            [name: 'notEqualTo', nArgs: 2],
                                            [name: 'numberFormat', nArgs: -2],
                                            [name: 'numberFormat2', nArgs: 5],
                                            [name: 'numGeometries', nArgs: 1],
                                            [name: 'numInteriorRing', nArgs: 1],
                                            [name: 'numPoints', nArgs: 1],
                                            [name: 'octagonalenvelope', nArgs: 1],
                                            [name: 'offset', nArgs: 3],
                                            [name: 'overlaps', nArgs: 2],
                                            [name: 'parameter', nArgs: -1],
                                            [name: 'parseBoolean', nArgs: 1],
                                            [name: 'parseDouble', nArgs: 1],
                                            [name: 'parseInt', nArgs: 1],
                                            [name: 'parseLong', nArgs: 1],
                                            [name: 'pi', nArgs: 0],
                                            [name: 'PointBuffers', nArgs: -1],
                                            [name: 'pointN', nArgs: 2],
                                            [name: 'PointStacker', nArgs: -7],
                                            [name: 'PolygonExtraction', nArgs: -1],
                                            [name: 'pow', nArgs: 2],
                                            [name: 'property', nArgs: 1],
                                            [name: 'PropertyExists', nArgs: 1],
                                            [name: 'Quantile', nArgs: 2],
                                            [name: 'Query', nArgs: -1],
                                            [name: 'random', nArgs: 0],
                                            [name: 'RangeLookup', nArgs: -1],
                                            [name: 'RasterAsPointCollection', nArgs: -1],
                                            [name: 'RasterZonalStatistics', nArgs: -2],
                                            [name: 'RasterZonalStatistics2', nArgs: -6],
                                            [name: 'Recode', nArgs: 5],
                                            [name: 'RectangularClip', nArgs: -2],
                                            [name: 'relate', nArgs: 2],
                                            [name: 'relatePattern', nArgs: 3],
                                            [name: 'Reproject', nArgs: -1],
                                            [name: 'rescaleToPixels', nArgs: -3],
                                            [name: 'rint', nArgs: 1],
                                            [name: 'round', nArgs: 1],
                                            [name: 'round_2', nArgs: 1],
                                            [name: 'roundDouble', nArgs: 1],
                                            [name: 'saturate', nArgs: -2],
                                            [name: 'ScaleCoverage', nArgs: -5],
                                            [name: 'setCRS', nArgs: 2],
                                            [name: 'shade', nArgs: 2],
                                            [name: 'Simplify', nArgs: 3],
                                            [name: 'sin', nArgs: 1],
                                            [name: 'Snap', nArgs: -2],
                                            [name: 'spin', nArgs: 2],
                                            [name: 'sqrt', nArgs: 1],
                                            [name: 'StandardDeviation', nArgs: 2],
                                            [name: 'startAngle', nArgs: 1],
                                            [name: 'startPoint', nArgs: 1],
                                            [name: 'strCapitalize', nArgs: 1],
                                            [name: 'strConcat', nArgs: 2],
                                            [name: 'strEndsWith', nArgs: 2],
                                            [name: 'strEqualsIgnoreCase', nArgs: 2],
                                            [name: 'strIndexOf', nArgs: 2],
                                            [name: 'stringTemplate', nArgs: 4],
                                            [name: 'strLastIndexOf', nArgs: 2],
                                            [name: 'strLength', nArgs: 1],
                                            [name: 'strMatches', nArgs: 2],
                                            [name: 'strPosition', nArgs: 3],
                                            [name: 'strReplace', nArgs: 4],
                                            [name: 'strStartsWith', nArgs: 2],
                                            [name: 'strSubstring', nArgs: 3],
                                            [name: 'strSubstringStart', nArgs: 2],
                                            [name: 'strToLowerCase', nArgs: 1],
                                            [name: 'strToUpperCase', nArgs: 1],
                                            [name: 'strTrim', nArgs: 1],
                                            [name: 'strTrim2', nArgs: 3],
                                            [name: 'strURLEncode', nArgs: -1],
                                            [name: 'StyleCoverage', nArgs: 2],
                                            [name: 'symDifference', nArgs: 2],
                                            [name: 'tan', nArgs: 1],
                                            [name: 'tint', nArgs: 2],
                                            [name: 'toDegrees', nArgs: 1],
                                            [name: 'toRadians', nArgs: 1],
                                            [name: 'touches', nArgs: 2],
                                            [name: 'toWKT', nArgs: 1],
                                            [name: 'Transform', nArgs: 2],
                                            [name: 'TransparencyFill', nArgs: 1],
                                            [name: 'union', nArgs: 2],
                                            [name: 'UnionFeatureCollection', nArgs: 2],
                                            [name: 'Unique', nArgs: 2],
                                            [name: 'UniqueInterval', nArgs: 2],
                                            [name: 'VectorToRaster', nArgs: -4],
                                            [name: 'VectorZonalStatistics', nArgs: 3],
                                            [name: 'vertices', nArgs: 1],
                                            [name: 'within', nArgs: 2]
                                    ]
                                    functions.each { function ->
                                        ogc.FunctionName(nArgs: function.nArgs, function.name)
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
        new StreamingMarkupBuilder(encoding: 'UTF-8').bind(content)
    }

    String describeFeatureType() {
        """
<?xml version="1.0" encoding="UTF-8"?>
<xsd:schema xmlns:gml="http://www.opengis.net/gml" xmlns:omar="http://omar.ossim.org" xmlns:xsd="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified" targetNamespace="http://omar.ossim.org">
  <xsd:import namespace="http://www.opengis.net/gml" schemaLocation="http://localhost:8080/geoserver/schemas/gml/3.1.1/base/gml.xsd"/>
  <xsd:complexType name="raster_entryType">
    <xsd:complexContent>
      <xsd:extension base="gml:AbstractFeatureType">
        <xsd:sequence>
          <xsd:element maxOccurs="1" minOccurs="1" name="id" nillable="false" type="xsd:long"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="version" nillable="false" type="xsd:long"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="organization" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="other_tags_xml" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="sun_azimuth" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="sun_elevation" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="crosses_dateline" nillable="true" type="xsd:boolean"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="image_representation" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="azimuth_angle" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="number_of_bands" nillable="false" type="xsd:int"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="tie_point_set" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="country_code" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="index_id" nillable="false" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="niirs" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="exclude_policy" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="filename" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="ingest_date" nillable="true" type="xsd:dateTime"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="image_category" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="width" nillable="false" type="xsd:long"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="grazing_angle" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="gsd_unit" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="cloud_cover" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="security_code" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="security_classification" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="receive_date" nillable="true" type="xsd:dateTime"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="product_id" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="class_name" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="wac_code" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="file_type" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="ground_geom" nillable="false" type="gml:MultiSurfacePropertyType"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="keep_forever" nillable="true" type="xsd:boolean"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="title" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="data_type" nillable="false" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="release_id" nillable="true" type="xsd:decimal"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="be_number" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="sensor_id" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="style_id" nillable="true" type="xsd:decimal"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="gsdy" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="target_id" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="valid_model" nillable="true" type="xsd:int"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="height" nillable="false" type="xsd:long"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="bit_depth" nillable="false" type="xsd:int"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="entry_id" nillable="false" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="gsdx" nillable="true" type="xsd:double"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="isorce" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="number_of_res_levels" nillable="true" type="xsd:int"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="image_id" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="access_date" nillable="true" type="xsd:dateTime"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="acquisition_date" nillable="true" type="xsd:dateTime"/>
          <xsd:element maxOccurs="1" minOccurs="0" name="mission_id" nillable="true" type="xsd:string"/>
          <xsd:element maxOccurs="1" minOccurs="1" name="raster_data_set_id" nillable="false" type="xsd:long"/>
        </xsd:sequence>
      </xsd:extension>
    </xsd:complexContent>
  </xsd:complexType>
  <xsd:element name="raster_entry" substitutionGroup="gml:_Feature" type="omar:raster_entryType"/>
</xsd:schema>
""".trim()
    }

    String getFeature() {
        """
<?xml version="1.0" encoding="UTF-8"?>
<wfs:FeatureCollection xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:omar="http://omar.ossim.org" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" numberOfFeatures="10" timeStamp="2018-08-14T17:40:04.184Z" xsi:schemaLocation="http://omar.ossim.org http://localhost:8080/geoserver/wfs?service=WFS&amp;version=1.1.0&amp;request=DescribeFeatureType&amp;typeName=omar%3Araster_entry http://www.opengis.net/wfs http://localhost:8080/geoserver/schemas/wfs/1.1.0/wfs.xsd">
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