<?php

require_once 'Query.php';

class AllNodesQuery extends Query{

    private $params;

    function __construct(array $params){
        $this->params = $params;
    }

    public function getQueryString() {

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