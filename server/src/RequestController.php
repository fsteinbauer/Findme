<?php


/**
 * Script query the Database from a mobile Application 
 */

class RequestController{

    private $dbConnector;
    private $getRequest;

    function __construct($dbConnector, $get){
        $this->dbConnector = $dbConnector;
        $this->dbConnector->connect();
        $this->getRequest = $get;
    }

    public function run(){
        $response = '';

        if(isset( $this->getRequest['task'])){
            switch ( $this->getRequest['task']) {
                case 'all_nodes':
                    $response = null;
                    break;

                case 'all_categories':
                    $response = (new CategoriesTask($this->dbConnector))->execute();
                    break;

                case 'single_node':
                    $response = null;
                    break;
            }
        } else {
            // No task property specified
            $response = array(
                'success' 	=> false,
                'error'		=> 'No parameter \'task\' specified.'
            );
            $response = json_encode($response);
        }

        // this will print the response
        return $response;
    }

    public function __destruct()
    {
        $this->dbConnector->close();
    }
}


?>
