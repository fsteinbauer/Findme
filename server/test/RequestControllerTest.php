<?php

require_once 'bootstrap.php';

class MockSQLConnector extends  SQLConnector
{
    public function executeQuery($query) {
        $output["categories"] = array();
        return $output;
    }

    public function connect() {

    }

    public function close() {

    }
}

class RequestControllerTest extends PHPUnit_Framework_TestCase {

    private  $requestController;
    private  $errorMsg = '{"success":false,"error":"No parameter \'task\' specified."}';

    public function setUp()
    {
        $getRequest = array();
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );
    }

    public function testNoTaskParameter()
    {
        $this->assertEquals($this->errorMsg, $this->requestController->run());
    }

    public function testAllCategories() {
        $getRequest = array( 'task' => 'all_categories');
        $this->requestController = new RequestController(new MockSQLConnector(),$getRequest );

        $this->assertNotEquals($this->errorMsg, $this->requestController->run());

        // TODO mock mysql fetching in Tasks??
        // TODO MockSQLConnector??

    }


}