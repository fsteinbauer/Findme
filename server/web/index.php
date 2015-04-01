<?php
    error_reporting(E_ALL);

    require_once('../src/AutoLoader.php');
    // Register the directory to your include files
    AutoLoader::registerDirectory('../src');


    //header('Content-Type: application/json');
    $query = new RequestController(new SQLConnector(), $_GET);
    print($query->run());

?>