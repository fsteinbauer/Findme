<?php 


$db = mysqli_connect('db1144.mydbserver.com', 'p209137', 'DebiUmu3', 'usr_p209137_1');
if (mysqli_connect_errno()) {
    die ('Konnte keine Verbindung zur Datenbank aufbauen: '.mysqli_connect_error().'('.mysqli_connect_errno().')');
}

$input = (array) simplexml_load_string(file_get_contents('/html/import/'.$_GET['file'].'.xml'));

$counter = 0;
foreach( $input['tr'] as $_xml ){
	$xml = $_xml->td;
	
	$response = json_decode( file_get_contents('http://dev.virtualearth.net/REST/v1/Locations?countryRegion=AT&postalCode='
		.$xml[2].'&addressLine='.urlencode($xml[4]).
		'&o=json&key=Au2rneVtKIrTOWqXPJEcrVO2z2UQmNxNldnxzHV1ux3wSHVpGSLQnc-3ovdz3sPg'),true);

	$coordintes = $response['resourceSets'][0]["resources"][0]["point"]["coordinates"];

	$sql = sprintf('INSERT INTO nodes ( lat, lon, timestamp, cid, name, user, country, zip, city, address)
	VALUES (%f, %f, NOW(), 7, "%s", 0, "AT", "%s", "%s", "%s")',
		$coordintes[0], 
		$coordintes[1],
		nameize($xml[1]),
		$xml[2],
		nameize($xml[3]),
		nameize($xml[4])
	);

	if (!mysqli_query($db,$sql)){
		die('Error: ' . mysqli_error($db));
	} else {
		echo nameize($xml[1])." was added<br>";
		$counter++;
	}
}
echo $counter." nodes were added";
mysqli_close($db);




function nameize($str,$a_char = array("'","-"," ","/")){    
    //$str contains the complete raw name string
    //$a_char is an array containing the characters we use as separators for capitalization. If you don't pass anything, there are three in there as default.
    $string = strtolower($str);
    foreach ($a_char as $temp){
        $pos = strpos($string,$temp);
        if ($pos){
            //we are in the loop because we found one of the special characters in the array, so lets split it up into chunks and capitalize each one.
            $mend = '';
            $a_split = explode($temp,$string);
            foreach ($a_split as $temp2){
                //capitalize each portion of the string which was separated at a special character
                $mend .= ucfirst($temp2).$temp;
                }
            $string = substr($mend,0,-1);
            }    
        }
    return ucfirst($string);
    }
