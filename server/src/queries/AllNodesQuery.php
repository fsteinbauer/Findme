<?php

require_once 'Query.php';

class AllNodesQuery extends Query{

    private $params;

    function __construct(array $params){
        $this->params = $params;
    }

    private function checkParams() {

        $error = array();

        if( !array_key_exists( 'lat', $this->params )){
            $error['success'] = false;
            $error['error'] = 'lat not specified';
        }

        if( !array_key_exists( 'lon', $this->params )){
            $error['success'] = false;
            $error['error'] = (isset($error['error'])?$error['error'].'; ':'') .'lon not specified';
        }

        if(isset($error['error'])) {
            return $error;
        }


        // If we have no min and max Lat/Lng, set some
        if( !array_key_exists( 'minLat', $this->params )){
            $this->params['minLat'] = -90;
        }
        if( !array_key_exists( 'maxLat',  $this->params )){
            $this->params['maxLat'] = 90;
        }
        if( !array_key_exists( 'minLng',  $this->params )){
            $this->params['minLng'] = -180;
        }
        if( !array_key_exists( 'maxLng',  $this->params )){
            $this->params['maxLng'] = 180;
        }
        if( !array_key_exists( 'cid',  $this->params )){
            $this->params['cid'] = 1;
        }

        return $error;
    }

    public function getQueryString() {

        $output = $this->checkParams();

        if(isset($output['error'])) {
            return $output;
        }
        // Build SQL
        $sql = "SELECT nid,
			( 6371 * acos( cos( radians(%f) ) * cos( radians( lat ) ) * cos( radians( lon )
				- radians(%f) ) + sin( radians(%f) ) * sin( radians( lat ) ) ) ) AS distance,
			name,
			timestamp,
			cid,
			lat,
			lon,
			user
		FROM nodes
		WHERE lat <= %f AND lat >= %f AND
			lon <= %f AND lon >= %f AND cid = %d
		HAVING distance < 500
		ORDER BY distance
		LIMIT 0 , 40";
        $sql = sprintf( $sql, $this->params['lat'], $this->params['lon'], $this->params['lat'],
            $this->params['maxLat'], $this->params['minLat'], $this->params['maxLng'], $this->params['minLng'],
            $this->params['cid']);


        $result = mysql_query($sql);

        // Check if we have an Error in our SQL syntax
        if (!$result) {
            $output = array(
                "success"		=> false,
                "error"			=> 'MYSQL_ERROR',
                "error_detail"	=> mysql_error()
            );
            return $output;
        }

        // check for empty result
        $output = array(
            "nodes"		=> array(),
            "success" 	=> true
        );
        if (mysql_num_rows($result)) {
            // looping through all results

            while($row=mysql_fetch_assoc($result))
                // push single product into final response array
                array_push($output["nodes"], $row);
        }
        return $output;

    }

}