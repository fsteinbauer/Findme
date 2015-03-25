<?php

abstract class AbstractTask {

    protected $sqlConnector;

    function __construct($sqlConnector){
        $this->sqlConnector = $sqlConnector;
    }

    public abstract function execute();
}