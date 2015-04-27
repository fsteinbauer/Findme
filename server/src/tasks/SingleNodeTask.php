<?php


class SingleNodeTask extends AbstractTask{

    public function execute(array $params=null) {

        $output = $this->sqlConnector->executeQuery(new SingleNodeQuery($params));

        return json_encode($output);

    }
}