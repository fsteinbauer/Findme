<?php

require_once '/../bootstrap.php';


class SingleNodeTaskTest extends PHPUnit_Framework_TestCase {
    private $task;

    private $errorMsgNoParameter = '{"success":false,"error":"No id specified"}';

    public function setUp()
    {
        $this->task = new SingleNodeTask(new MockSQLConnector());
    }

    public function testNoParametersFails()
    {
        $actual = $this->task->execute(array());
        $this->assertEquals($this->errorMsgNoParameter, $actual);
    }

    public function testIdAsStringFails()
    {
        $actual = $this->task->execute(array('id' => 'deimama'));
        $this->assertEquals($this->errorMsgNoParameter, $actual);
    }

    public function testIdNumberSucceeds()
    {
        $actual = $this->task->execute(array('id' => '5'));
        $this->assertEquals(json_encode(array('result' => array())), $actual);
    }

}