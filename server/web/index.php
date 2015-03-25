<?php
    error_reporting(E_ALL);

    // Load the different Functions
    foreach (glob("../src/*.php") as $filename)
    {
        require_once $filename;
    }

    foreach (glob("../src/*/*.php") as $filename)
    {
        require_once $filename;
    }


    header('Content-Type: application/json');
    $query = new RequestController(new SQLConnector(), $_GET);
    print($query->run());

?>