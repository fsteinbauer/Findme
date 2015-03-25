<?php

class SQLConnector {

    private $config;

    function __construct(){
        $this->config = parse_ini_file('../database.ini');
        var_dump($this->config);
    }
    
    public function executeQuery($query) {
        $sql = $query->getQueryString();
        $result = mysql_query($sql);
        return $result;
    }

    public function connect() {
        // Connect to DB
        mysql_connect($this->config['dbHost'], $this->config['dbUser'], $this->config['dbPass']);
        mysql_select_db($this->config['dbName']);
    }

    public function close() {
        // Close the connection
        mysql_close();
    }
}