package monolith.geoscript

import javax.inject.Singleton

@Singleton
class GeoscriptService
{
	def namespaces = [
		cite: "http://www.opengeospatial.net/cite",
		tiger: "http://www.census.gov",
		nurc: "http://www.nurc.nato.int",
		sde: "http://geoserver.sf.net",
		'it.geosolutions': "http://www.geo-solutions.it",
		topp: "http://www.openplans.org/topp",
		sf: "http://www.openplans.org/spearfish",
		omar: "http://omar.ossim.org",
		streetview: "http://streetview.ossim.io"
	]
	
	def workspaces = [
		'nyc',
		'sf',
		'shapefiles',
		'taz_shapes'
	]
	
	def features = [ [
		namespace: namespaces['tiger'],
		name: 'poly_landmarks',
		title: 'Manhattan (NY) landmarks',
		description: 'Manhattan landmarks, identifies water, lakes, parks, interesting buildilngs',
		keywords: [
			'landmarks',
			'DS_poly_landmarks',
			'manhattan',
			'poly_landmarks'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: -74.047185,
			minY: 40.679648,
			maxX: -73.90782,
			maxY: 40.882078
		]
	], [
		namespace: namespaces['tiger'],
		name: 'poi',
		title: 'Manhattan (NY) points of interest',
		description: 'Points of interest in New York, New York (on Manhattan). One of the attributes contains the name of a file with a picture of the point of interest.',
		keywords: [
			'poi',
			'Manhattan',
			'DS_poi',
			'points_of_interest'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: -74.0118315772888,
			minY: 40.70754683896324,
			maxX: -74.00857344353275,
			maxY: 40.711945649065406
		]
	], [
		namespace: namespaces['tiger'],
		name: 'tiger_roads',
		title: 'Manhattan (NY) roads',
		description: 'Highly simplified road layout of Manhattan in New York..',
		keywords: [
			'DS_tiger_roads',
			'tiger_roads',
			'roads'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: -74.02722,
			minY: 40.684221,
			maxX: -73.907005,
			maxY: 40.878178
		]
	
	], [
		namespace: namespaces['sf'],
		name: 'archsites',
		title: 'Spearfish archeological sites',
		description: 'Sample data from GRASS, archeological sites location, Spearfish, South Dakota, USA',
		keywords: [
			'archsites',
			'spearfish',
			'sfArchsites',
			'archeology'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#26713',
		bounds: [
			minX: -103.8725637911543,
			minY: 44.37740330855979,
			maxX: -103.63794182141925,
			maxY: 44.48804280772808
		]
	], [
		namespace: namespaces['sf'],
		name: 'bugsites',
		title: 'Spearfish bug locations',
		description: 'Sample data from GRASS, bug sites location, Spearfish, South Dakota, USA',
		keywords: [
			'spearfish',
			'sfBugsites',
			'insects',
			'bugsites',
			'tiger_beetles'
		
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#26713',
		bounds: [
			minX: -103.86796131703647,
			minY: 44.373938816704396,
			maxX: -103.63773523234195,
			maxY: 44.43418821380063
		]
	], [
		namespace: namespaces['sf'],
		name: 'restricted',
		title: 'Spearfish restricted areas',
		description: 'Sample data from GRASS, restricted areas, Spearfish, South Dakota, USA',
		keywords: [
			'spearfish',
			'restricted',
			'areas',
			'sfRestricted'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#26713',
		bounds: [
			minX: -103.85057172920756,
			minY: 44.39436387625042,
			maxX: -103.74741494853805,
			maxY: 44.48215752041131
		]
	], [
		namespace: namespaces['sf'],
		name: 'roads',
		title: 'Spearfish roads',
		description: 'Sample data from GRASS, road layout, Spearfish, South Dakota, USA',
		keywords: [
			'sfRoads',
			'spearfish',
			'roads'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#26713',
		bounds: [
			minX: -103.87741691493184,
			minY: 44.37087275281798,
			maxX: -103.62231404880659,
			maxY: 44.50015918338962
		]
	], [
		namespace: namespaces['sf'],
		name: 'streams',
		title: 'Spearfish streams',
		description: 'Sample data from GRASS, streams, Spearfish, South Dakota, USA',
		keywords: [
			'spearfish',
			'sfStreams',
			'streams'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#26713',
		bounds: [
			minX: -103.87789019829768,
			minY: 44.372335260095554,
			maxX: -103.62287788915457,
			maxY: 44.502218486214815
		]
	], [
		namespace: namespaces['topp'],
		name: 'tasmania_cities',
		title: 'Tasmania cities',
		description: 'Cities in Tasmania (actually, just the capital)',
		keywords: [
			'cities',
			'Tasmania'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: 145.19754,
			minY: -43.423512,
			maxX: 148.27298000000002,
			maxY: -40.852802
		]
	], [
		namespace: namespaces['topp'],
		name: 'tasmania_roads',
		title: 'Tasmania roads',
		description: 'Main Tasmania roads',
		keywords: [
			'Roads',
			'Tasmania'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: 145.19754,
			minY: -43.423512,
			maxX: 148.27298000000002,
			maxY: -40.852802
		]
	], [
		namespace: namespaces['topp'],
		name: 'tasmania_state_boundaries',
		title: 'Tasmania state boundaries',
		description: 'Tasmania state boundaries',
		keywords: [
			'boundaries',
			'tasmania_state_boundaries',
			'Tasmania'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: 143.83482400000003,
			minY: -43.648056,
			maxX: 148.47914100000003,
			maxY: -39.573891
		]
	], [
		namespace: namespaces['topp'],
		name: 'tasmania_water_bodies',
		title: 'Tasmania water bodies',
		description: 'Tasmania water bodies',
		keywords: [
			'Lakes',
			'Bodies',
			'Australia',
			'Water',
			'Tasmania'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: 145.97161899999998,
			minY: -43.031944,
			maxX: 147.219696,
			maxY: -41.775558
		]
	], [
		namespace: namespaces['topp'],
		name: 'states',
		title: 'USA Population',
		description: 'This is some census data on the states.',
		keywords: [
			'census',
			'united',
			'boundaries',
			'state',
			'states'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: -124.731422,
			minY: 24.955967,
			maxX: -66.969849,
			maxY: 49.371735
		]
	], [
		namespace: namespaces['tiger'],
		name: 'giant_polygon',
		title: 'World rectangle',
		description: 'A simple rectangular polygon covering most of the world, it\'s only used for the purpose of providing a background (WMS bgcolor could be used instead)',
		keywords: [
			'DS_giant_polygon',
			'giant_polygon'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: -180.0,
			minY: -90.0,
			maxX: 180.0,
			maxY: 90.0
		]
	], [
		namespace: namespaces['omar'],
		name: 'raster_entry',
		title: 'raster_entry',
		description: '',
		keywords: [
			'features',
			'raster_entry'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: -183.20295715332,
			minY: -92.3009185791016,
			maxX: 183.197738647461,
			maxY: 92.3120803833008
		]
	], [
		namespace: namespaces['streetview'],
		name: 'road_streetview_subset',
		title: 'road_streetview_subset',
		description: '',
		keywords: [
			'features',
			'road_streetview_subset'
		],
		proj: 'http://www.opengis.net/gml/srs/epsg.xml#4326',
		bounds: [
			minX: 116.368103027344,
			minY: 39.9055595397949,
			maxX: 116.396774291992,
			maxY: 39.9064750671387
		]
	] ]
	def functions = [
		[ name: 'abs', nArgs: 1 ],
		[ name: 'abs_2', nArgs: 1 ],
		[ name: 'abs_3', nArgs: 1 ],
		[ name: 'abs_4', nArgs: 1 ],
		[ name: 'acos', nArgs: 1 ],
		[ name: 'AddCoverages', nArgs: 2 ],
		[ name: 'Affine', nArgs: -1 ],
		[ name: 'Aggregate', nArgs: -2 ],
		[ name: 'Area', nArgs: 1 ],
		[ name: 'area2', nArgs: 1 ],
		[ name: 'AreaGrid', nArgs: 3 ],
		[ name: 'asin', nArgs: 1 ],
		[ name: 'atan', nArgs: 1 ],
		[ name: 'atan2', nArgs: 2 ],
		[ name: 'attributeCount', nArgs: 1 ],
		[ name: 'BandMerge', nArgs: -1 ],
		[ name: 'BandSelect', nArgs: -2 ],
		[ name: 'BarnesSurface', nArgs: -6 ],
		[ name: 'between', nArgs: 3 ],
		[ name: 'boundary', nArgs: 1 ],
		[ name: 'boundaryDimension', nArgs: 1 ],
		[ name: 'Bounds', nArgs: 1 ],
		[ name: 'buffer', nArgs: 2 ],
		[ name: 'BufferFeatureCollection', nArgs: -2 ],
		[ name: 'bufferWithSegments', nArgs: 3 ],
		[ name: 'Categorize', nArgs: 7 ],
		[ name: 'ceil', nArgs: 1 ],
		[ name: 'centroid', nArgs: 1 ],
		[ name: 'classify', nArgs: 2 ],
		[ name: 'Clip', nArgs: -2 ],
		[ name: 'CollectGeometries', nArgs: 1 ],
		[ name: 'Collection_Average', nArgs: 1 ],
		[ name: 'Collection_Bounds', nArgs: 1 ],
		[ name: 'Collection_Count', nArgs: 0 ],
		[ name: 'Collection_Max', nArgs: 1 ],
		[ name: 'Collection_Median', nArgs: 1 ],
		[ name: 'Collection_Min', nArgs: 1 ],
		[ name: 'Collection_Nearest', nArgs: 1 ],
		[ name: 'Collection_Sum', nArgs: 1 ],
		[ name: 'Collection_Unique', nArgs: 1 ],
		[ name: 'Concatenate', nArgs: -2 ],
		[ name: 'contains', nArgs: 2 ],
		[ name: 'Contour', nArgs: -1 ],
		[ name: 'contrast', nArgs: -1 ],
		[ name: 'convert', nArgs: 2 ],
		[ name: 'convexHull', nArgs: 1 ],
		[ name: 'ConvolveCoverage', nArgs: -1 ],
		[ name: 'cos', nArgs: 1 ],
		[ name: 'Count', nArgs: 1 ],
		[ name: 'CoverageClassStats', nArgs: -1 ],
		[ name: 'CropCoverage', nArgs: 2 ],
		[ name: 'crosses', nArgs: 2 ],
		[ name: 'darken', nArgs: -2 ],
		[ name: 'dateFormat', nArgs: 2 ],
		[ name: 'dateParse', nArgs: 2 ],
		[ name: 'desaturate', nArgs: -2 ],
		[ name: 'difference', nArgs: 2 ],
		[ name: 'dimension', nArgs: 1 ],
		[ name: 'disjoint', nArgs: 2 ],
		[ name: 'disjoint3D', nArgs: 2 ],
		[ name: 'distance', nArgs: 2 ],
		[ name: 'distance3D', nArgs: 2 ],
		[ name: 'double2bool', nArgs: 1 ],
		[ name: 'endAngle', nArgs: 1 ],
		[ name: 'endPoint', nArgs: 1 ],
		[ name: 'env', nArgs: 1 ],
		[ name: 'envelope', nArgs: 1 ],
		[ name: 'EqualInterval', nArgs: 2 ],
		[ name: 'equalsExact', nArgs: 2 ],
		[ name: 'equalsExactTolerance', nArgs: 3 ],
		[ name: 'equalTo', nArgs: 2 ],
		[ name: 'exp', nArgs: 1 ],
		[ name: 'exteriorRing', nArgs: 1 ],
		[ name: 'Feature', nArgs: 3 ],
		[ name: 'FeatureClassStats', nArgs: -2 ],
		[ name: 'floor', nArgs: 1 ],
		[ name: 'geometry', nArgs: 0 ],
		[ name: 'geometryType', nArgs: 1 ],
		[ name: 'geomFromWKT', nArgs: 1 ],
		[ name: 'geomLength', nArgs: 1 ],
		[ name: 'getGeometryN', nArgs: 2 ],
		[ name: 'getX', nArgs: 1 ],
		[ name: 'getY', nArgs: 1 ],
		[ name: 'getz', nArgs: 1 ],
		[ name: 'grayscale', nArgs: 1 ],
		[ name: 'greaterEqualThan', nArgs: 2 ],
		[ name: 'greaterThan', nArgs: 2 ],
		[ name: 'Grid', nArgs: -3 ],
		[ name: 'Heatmap', nArgs: -5 ],
		[ name: 'hsl', nArgs: 3 ],
		[ name: 'id', nArgs: 0 ],
		[ name: 'IEEEremainder', nArgs: 2 ],
		[ name: 'if_then_else', nArgs: 3 ],
		[ name: 'in', nArgs: -2 ],
		[ name: 'in10', nArgs: 11 ],
		[ name: 'in2', nArgs: 3 ],
		[ name: 'in3', nArgs: 4 ],
		[ name: 'in4', nArgs: 5 ],
		[ name: 'in5', nArgs: 6 ],
		[ name: 'in6', nArgs: 7 ],
		[ name: 'in7', nArgs: 8 ],
		[ name: 'in8', nArgs: 9 ],
		[ name: 'in9', nArgs: 10 ],
		[ name: 'InclusionFeatureCollection', nArgs: 2 ],
		[ name: 'int2bbool', nArgs: 1 ],
		[ name: 'int2ddouble', nArgs: 1 ],
		[ name: 'interiorPoint', nArgs: 1 ],
		[ name: 'interiorRingN', nArgs: 2 ],
		[ name: 'Interpolate', nArgs: -5 ],
		[ name: 'intersection', nArgs: 2 ],
		[ name: 'IntersectionFeatureCollection', nArgs: -2 ],
		[ name: 'intersects', nArgs: 2 ],
		[ name: 'intersects3D', nArgs: 2 ],
		[ name: 'isClosed', nArgs: 1 ],
		[ name: 'isCoverage', nArgs: 0 ],
		[ name: 'isEmpty', nArgs: 1 ],
		[ name: 'isInstanceOf', nArgs: 1 ],
		[ name: 'isLike', nArgs: 2 ],
		[ name: 'isNull', nArgs: 1 ],
		[ name: 'isometric', nArgs: 2 ],
		[ name: 'isRing', nArgs: 1 ],
		[ name: 'isSimple', nArgs: 1 ],
		[ name: 'isValid', nArgs: 1 ],
		[ name: 'isWithinDistance', nArgs: 3 ],
		[ name: 'isWithinDistance3D', nArgs: 3 ],
		[ name: 'Jenks', nArgs: 2 ],
		[ name: 'length', nArgs: 1 ],
		[ name: 'lessEqualThan', nArgs: 2 ],
		[ name: 'lessThan', nArgs: 2 ],
		[ name: 'lighten', nArgs: -2 ],
		[ name: 'list', nArgs: -1 ],
		[ name: 'listMultiply', nArgs: 2 ],
		[ name: 'log', nArgs: 1 ],
		[ name: 'LRSGeocode', nArgs: 4 ],
		[ name: 'LRSMeasure', nArgs: -4 ],
		[ name: 'LRSSegment', nArgs: 5 ],
		[ name: 'max', nArgs: 2 ],
		[ name: 'max_2', nArgs: 2 ],
		[ name: 'max_3', nArgs: 2 ],
		[ name: 'max_4', nArgs: 2 ],
		[ name: 'min', nArgs: 2 ],
		[ name: 'min_2', nArgs: 2 ],
		[ name: 'min_3', nArgs: 2 ],
		[ name: 'min_4', nArgs: 2 ],
		[ name: 'mincircle', nArgs: 1 ],
		[ name: 'minimumdiameter', nArgs: 1 ],
		[ name: 'minrectangle', nArgs: 1 ],
		[ name: 'mix', nArgs: 3 ],
		[ name: 'modulo', nArgs: 2 ],
		[ name: 'MultiplyCoverages', nArgs: 2 ],
		[ name: 'Nearest', nArgs: -2 ],
		[ name: 'NormalizeCoverage', nArgs: 1 ],
		[ name: 'not', nArgs: 1 ],
		[ name: 'notEqualTo', nArgs: 2 ],
		[ name: 'numberFormat', nArgs: -2 ],
		[ name: 'numberFormat2', nArgs: 5 ],
		[ name: 'numGeometries', nArgs: 1 ],
		[ name: 'numInteriorRing', nArgs: 1 ],
		[ name: 'numPoints', nArgs: 1 ],
		[ name: 'octagonalenvelope', nArgs: 1 ],
		[ name: 'offset', nArgs: 3 ],
		[ name: 'overlaps', nArgs: 2 ],
		[ name: 'parameter', nArgs: -1 ],
		[ name: 'parseBoolean', nArgs: 1 ],
		[ name: 'parseDouble', nArgs: 1 ],
		[ name: 'parseInt', nArgs: 1 ],
		[ name: 'parseLong', nArgs: 1 ],
		[ name: 'pi', nArgs: 0 ],
		[ name: 'PointBuffers', nArgs: -1 ],
		[ name: 'pointN', nArgs: 2 ],
		[ name: 'PointStacker', nArgs: -7 ],
		[ name: 'PolygonExtraction', nArgs: -1 ],
		[ name: 'pow', nArgs: 2 ],
		[ name: 'property', nArgs: 1 ],
		[ name: 'PropertyExists', nArgs: 1 ],
		[ name: 'Quantile', nArgs: 2 ],
		[ name: 'Query', nArgs: -1 ],
		[ name: 'random', nArgs: 0 ],
		[ name: 'RangeLookup', nArgs: -1 ],
		[ name: 'RasterAsPointCollection', nArgs: -1 ],
		[ name: 'RasterZonalStatistics', nArgs: -2 ],
		[ name: 'RasterZonalStatistics2', nArgs: -6 ],
		[ name: 'Recode', nArgs: 5 ],
		[ name: 'RectangularClip', nArgs: -2 ],
		[ name: 'relate', nArgs: 2 ],
		[ name: 'relatePattern', nArgs: 3 ],
		[ name: 'Reproject', nArgs: -1 ],
		[ name: 'rescaleToPixels', nArgs: -3 ],
		[ name: 'rint', nArgs: 1 ],
		[ name: 'round', nArgs: 1 ],
		[ name: 'round_2', nArgs: 1 ],
		[ name: 'roundDouble', nArgs: 1 ],
		[ name: 'saturate', nArgs: -2 ],
		[ name: 'ScaleCoverage', nArgs: -5 ],
		[ name: 'setCRS', nArgs: 2 ],
		[ name: 'shade', nArgs: 2 ],
		[ name: 'Simplify', nArgs: 3 ],
		[ name: 'sin', nArgs: 1 ],
		[ name: 'Snap', nArgs: -2 ],
		[ name: 'spin', nArgs: 2 ],
		[ name: 'sqrt', nArgs: 1 ],
		[ name: 'StandardDeviation', nArgs: 2 ],
		[ name: 'startAngle', nArgs: 1 ],
		[ name: 'startPoint', nArgs: 1 ],
		[ name: 'strCapitalize', nArgs: 1 ],
		[ name: 'strConcat', nArgs: 2 ],
		[ name: 'strEndsWith', nArgs: 2 ],
		[ name: 'strEqualsIgnoreCase', nArgs: 2 ],
		[ name: 'strIndexOf', nArgs: 2 ],
		[ name: 'stringTemplate', nArgs: 4 ],
		[ name: 'strLastIndexOf', nArgs: 2 ],
		[ name: 'strLength', nArgs: 1 ],
		[ name: 'strMatches', nArgs: 2 ],
		[ name: 'strPosition', nArgs: 3 ],
		[ name: 'strReplace', nArgs: 4 ],
		[ name: 'strStartsWith', nArgs: 2 ],
		[ name: 'strSubstring', nArgs: 3 ],
		[ name: 'strSubstringStart', nArgs: 2 ],
		[ name: 'strToLowerCase', nArgs: 1 ],
		[ name: 'strToUpperCase', nArgs: 1 ],
		[ name: 'strTrim', nArgs: 1 ],
		[ name: 'strTrim2', nArgs: 3 ],
		[ name: 'strURLEncode', nArgs: -1 ],
		[ name: 'StyleCoverage', nArgs: 2 ],
		[ name: 'symDifference', nArgs: 2 ],
		[ name: 'tan', nArgs: 1 ],
		[ name: 'tint', nArgs: 2 ],
		[ name: 'toDegrees', nArgs: 1 ],
		[ name: 'toRadians', nArgs: 1 ],
		[ name: 'touches', nArgs: 2 ],
		[ name: 'toWKT', nArgs: 1 ],
		[ name: 'Transform', nArgs: 2 ],
		[ name: 'TransparencyFill', nArgs: 1 ],
		[ name: 'union', nArgs: 2 ],
		[ name: 'UnionFeatureCollection', nArgs: 2 ],
		[ name: 'Unique', nArgs: 2 ],
		[ name: 'UniqueInterval', nArgs: 2 ],
		[ name: 'VectorToRaster', nArgs: -4 ],
		[ name: 'VectorZonalStatistics', nArgs: 3 ],
		[ name: 'vertices', nArgs: 1 ],
		[ name: 'within', nArgs: 2 ]
	]
}