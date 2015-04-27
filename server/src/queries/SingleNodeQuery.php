<?php

require_once 'Query.php';

class SingleNodeQuery extends Query{

    private $params;

    function __construct(array $params){
        $this->params = $params;
    }

    private function checkParams() {
        $error = array();

        if( !isset($this->params['id']) || !is_numeric($this->params['id']) ){
            $error['success'] = false;
            $error['error'] = "No id specified";
        }

        return $error;

    }

    public function getQueryString() {

        $output = $this->checkParams();


        if(isset($output['error'])) {
            return $output;
        }



        // Build SQL
        $sql = "SELECT *
		FROM nodes
		WHERE nid = %d";
        $sql = sprintf( $sql, $this->params['id']);


        $result = mysql_query($sql);

        // check for empty result
        $output["node"] = array();
        if (mysql_num_rows($result) > 0) {
            $output["success"] = "1";

            while($row=mysql_fetch_assoc($result))
                // push single product into final response array
                array_push($output["node"], $row);
        } else {
            $output = array(
                "success"	=> false,
                "error"		=> "NO_RESULT"
            );
            return $output;
        }
        return $output;



    }

}