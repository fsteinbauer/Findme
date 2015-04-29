<?php

require_once '/../bootstrap.php';


class AllNodesTaskTest extends PHPUnit_Framework_TestCase {
    private  $task;

    private $errorMsgNoLatParameter = '{"success":false,"error":"lat not specified"}';
    private $errorMsgNoLonParameter = '{"success":false,"error":"lon not specified"}';
    private $errorMsgNoLatLonParameter = '{"success":false,"error":"lat not specified; lon not specified"}';

    public function setUp()
    {
        $this->task = new AllNodesTask(new MockSQLConnector());
    }

    public function testNoLatParameter()
    {
        $getRequest = array('lon' => '37');
        $actual = $this->task->execute($getRequest);

        $this->assertEquals($this->errorMsgNoLatParameter, $actual);
    }

    public function testNoLonParameter()
    {
        $getRequest = array('lat' => '37');
        $actual = $this->task->execute($getRequest);

        $this->assertEquals($this->errorMsgNoLonParameter, $actual);
    }

    public function testNoParameters()
    {
        $getRequest = array();
        $actual = $this->task->execute($getRequest);

        $this->assertEquals($this->errorMsgNoLatLonParameter, $actual);
    }

    public function testLatLonSpecified()
    {
        $getRequest = array('lat' => '13', 'lon' => '37');
        $actual = $this->task->execute($getRequest);

        $this->assertEquals(json_encode(array('result' => array())), $actual);
    }
}