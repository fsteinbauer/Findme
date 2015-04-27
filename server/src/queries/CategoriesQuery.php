<?php

require_once 'Query.php';

class CategoriesQuery extends Query{

    function __construct(){

    }

    public function getQueryString() {
        $sql =  "SELECT cid,
				   name
		FROM category
		WHERE hidden = 0";

        $result = mysql_query($sql);

        if (!$result) {
            $output = array(
                "success"		=> false,
                "error"			=> 'MYSQL_ERROR',
                "error_detail"	=> mysql_error()
            );
            return $output;
        }

        // check for empty result
        $output["categories"] = array();
        $output["success"] = true;
        if (mysql_num_rows($result)) {
            // looping through all results

            while($row=mysql_fetch_assoc($result))
                // push single product into final response array
                array_push($output["categories"], $row);
        }

        return $output;

    }

}