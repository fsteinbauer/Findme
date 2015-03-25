<?php

require_once 'Query.php';

class CategoriesQuery extends Query{

    function __construct(){

    }

    public function getQueryString() {
        return  "SELECT cid,
				   name
		FROM category
		WHERE hidden = 0";
    }

}