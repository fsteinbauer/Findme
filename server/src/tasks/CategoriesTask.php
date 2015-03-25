<?php


class CategoriesTask extends AbstractTask{

    public function execute() {

        $result = $this->sqlConnector->executeQuery(new CategoriesQuery());

        if (!$result) {
            $output = array(
                "success"		=> '0',
                "error"			=> 'MYSQL_ERROR',
                "error_detail"	=> mysql_error()
            );
            return $output;
        }

        // check for empty result
        $output["categories"] = array();
        if (mysql_num_rows($result)) {
            // looping through all results

            while($row=mysql_fetch_assoc($result))
                // push single product into final response array
                array_push($output["categories"], $row);
        }

        return json_encode($output);
    }
}