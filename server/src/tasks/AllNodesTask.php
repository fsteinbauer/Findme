<?php


class AllNodesTask extends AbstractTask{

    public function execute(array $params=null) {

        $output = $this->sqlConnector->executeQuery(new AllNodesQuery($params));

        return json_encode($output);
    }
}