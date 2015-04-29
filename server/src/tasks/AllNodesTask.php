<?php


class AllNodesTask extends AbstractTask{

    public function execute(array $params=null) {

        if(isset($params)) {

            $output = $this->checkParams($params);

            if (isset($output['error'])) {
                return json_encode($output);
            }
        }

        // If we have no min and max Lat/Lng, set some
        if( !array_key_exists( 'minLat', $params )){
            $params['minLat'] = -90;
        }
        if( !array_key_exists( 'maxLat',  $params )){
            $params['maxLat'] = 90;
        }
        if( !array_key_exists( 'minLng',  $params )){
            $params['minLng'] = -180;
        }
        if( !array_key_exists( 'maxLng',  $params )){
            $params['maxLng'] = 180;
        }
        if( !array_key_exists( 'cid',  $params )){
            $params['cid'] = 1;
        }

        $output = $this->sqlConnector->executeQuery(new AllNodesQuery($params));

        return json_encode($output);
    }

    private function checkParams(array $params) {

        $error = array();

        if( !array_key_exists( 'lat', $params )){
            $error['success'] = false;
            $error['error'] = 'lat not specified';
        }

        if( !array_key_exists( 'lon', $params )){
            $error['success'] = false;
            $error['error'] = (isset($error['error'])?$error['error'].'; ':'') .'lon not specified';
        }

        return $error;
    }
}