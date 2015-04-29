<?php

require_once '/../bootstrap.php';


class CategoriesTaskTest extends PHPUnit_Framework_TestCase {
    private  $task;


    public function setUp()
    {
        $this->task = new CategoriesTask(new MockSQLConnector());
    }

    public function testNoParametersSucceeds()
    {
        $actual = $this->task->execute();

        $this->assertEquals(json_encode(array('result' => array())), $actual);
    }

}