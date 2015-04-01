<?php


class CategoriesTask extends AbstractTask{

    public function execute() {

        $output = $this->sqlConnector->executeQuery(new CategoriesQuery());

        return json_encode($output);
    }
}