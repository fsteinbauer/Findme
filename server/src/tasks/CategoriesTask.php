<?php


class CategoriesTask extends AbstractTask{

    public function execute(array $params=null) {

        $output = $this->sqlConnector->executeQuery(new CategoriesQuery());

        return json_encode($output);
    }
}