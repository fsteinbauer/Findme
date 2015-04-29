<?php

require_once 'bootstrap.php';


class MockSQLConnector extends  SQLConnector
{
    public function executeQuery($query) {
        $output["result"] = array();
        return $output;
    }

    public function connect() {

    }

    public function close() {

    }
}

class RequestControllerTest extends PHPUnit_Framework_TestCase {

    private $requestController;

    private $errorMsgNoTaskParam = '{"success":false,"error":"No parameter \'task\' specified."}';
    private $errorMsgNoLatLon = '{"success":false,"error":"lat not specified; lon not specified"}';
    private $errorMsgNoLon = '{"success":false,"error":"lon not specified"}';
    private $errorMsgNoLat = '{"success":false,"error":"lat not specified"}';
    private $errorMsgNoId = '{"success":false,"error":"No id specified"}';

    public function setUp()
    {
        $getRequest = array();
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );
    }

    public function testNoTaskParameter()
    {
        $this->assertEquals($this->errorMsgNoTaskParam, $this->requestController->run());
    }

    public function testAllCategories() {
        $getRequest = array( 'task' => 'all_categories');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertContains("\"result\"", $this->requestController->run());
    }

    public function testAllNodesNoParams() {
        $getRequest = array( 'task' => 'all_nodes');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertEquals($this->errorMsgNoLatLon, $this->requestController->run());

    }

    public function testAllNodesNoLon() {
        $getRequest = array( 'task' => 'all_nodes', 'lat' => '47');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertEquals($this->errorMsgNoLon, $this->requestController->run());
    }

    public function testAllNodesNoLat() {
        $getRequest = array( 'task' => 'all_nodes', 'lon' => '15');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertEquals($this->errorMsgNoLat, $this->requestController->run());
    }

   public function testAllNodes() {
        $getRequest = array( 'task' => 'all_nodes', 'lon' => '15', 'lat' => '47');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertContains("\"result\"",  $this->requestController->run());
    }


    public function testSingleNodeNoId() {
        $getRequest = array( 'task' => 'single_node');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertEquals($this->errorMsgNoId, $this->requestController->run());
    }

    public function testSingleNode() {
        $getRequest = array( 'task' => 'single_node', 'id' => '5');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertContains("\"result\"", $this->requestController->run());
    }


}