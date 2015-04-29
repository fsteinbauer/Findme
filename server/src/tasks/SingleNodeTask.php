<?php


class SingleNodeTask extends AbstractTask{

    public function execute(array $params=null) {

        if(isset($params)) {
            $output = $this->checkParams($params);

            if (isset($output['error'])) {
                return json_encode($output);
            }
        }

        $output = $this->sqlConnector->executeQuery(new SingleNodeQuery($params));

        return json_encode($output);

    }

    private function checkParams(array $params) {
        $error = array();

        if( !isset($params['id']) || !is_numeric($params['id']) ){
            $error['success'] = false;
            $error['error'] = "No id specified";
        }

        return $error;

    }
}