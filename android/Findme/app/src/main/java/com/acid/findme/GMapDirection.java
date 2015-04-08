package com.acid.findme;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.acid.findme.exception.GMapDirectionException;
import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;

public class GMapDirection{
	
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";

    public final static String GMAP_DIRECTION_TAG = "GMapDirection";
	
	private String url;
	private List<NameValuePair> params = null;
	private LoadDocument loadDocument = null;
	
	public GMapDirection() {
	}

	public Document getDocument(LatLng start, LatLng end, String mode) throws GMapDirectionException {
	    url = "http://maps.googleapis.com/maps/api/directions/xml";
	    
	    params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("origin", start.latitude + "," + start.longitude));
		params.add(new BasicNameValuePair("destination", end.latitude + "," + end.longitude));
		params.add(new BasicNameValuePair("sensor", "false"));
		params.add(new BasicNameValuePair("units", "metric"));
		params.add(new BasicNameValuePair("mode", mode));
		
		String paramString = URLEncodedUtils.format(params, "utf-8");
	    url += "?" + paramString;
	    
	    loadDocument = new LoadDocument();
	    try {
			return loadDocument.execute(url).get();
		} catch (InterruptedException e) {
            Log.e(GMAP_DIRECTION_TAG, " InterruptedException: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
		} catch (ExecutionException e) {
            Log.e(GMAP_DIRECTION_TAG, " ExecutionException: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
		}
	}
	
	
	/**
	 * Background Async Task to load up the Spinner
	 */
	class LoadDocument extends AsyncTask<String, String, Document> {
	
		protected Document doInBackground(String... urls) {
			try {
				// request method is GET
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        
		        HttpGet httpGet = new HttpGet(urls[0]);
		
		        HttpResponse httpResponse = httpClient.execute(httpGet);
		        HttpEntity httpEntity = httpResponse.getEntity();
		        InputStream is = httpEntity.getContent();
		        DocumentBuilder builder = DocumentBuilderFactory.newInstance()
		                .newDocumentBuilder();
		        Document doc = builder.parse(is);
		        return doc;
			
			} catch (UnsupportedEncodingException e) {
                Log.e(GMAP_DIRECTION_TAG, " UnsupportedEncodingException: " + e.toString());
                e.printStackTrace();
                return null;
			} catch (ClientProtocolException e) {
                Log.e(GMAP_DIRECTION_TAG, " ClientProtocolException: " + e.toString());
                e.printStackTrace();
                return null;
			} catch (IOException e) {
                Log.e(GMAP_DIRECTION_TAG, " IOException: " + e.toString());
                e.printStackTrace();
                return null;
			} catch (ParserConfigurationException e) {
                Log.e(GMAP_DIRECTION_TAG, " ParserConfigurationException: " + e.toString());
                e.printStackTrace();
                return null;
			} catch (SAXException e) {
                Log.e(GMAP_DIRECTION_TAG, " SAXException: " + e.toString());
                e.printStackTrace();
                return null;
			}
		}
	}
	
	public String getDurationText(Document doc) throws GMapDirectionException {
	    try {
	
	        NodeList nl1 = doc.getElementsByTagName("duration");
	        Node node1 = nl1.item(0);
	        NodeList nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
	        Log.i("DurationText", node2.getTextContent());
	        return node2.getTextContent();
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	    }
	}
	
	public int getDurationValue(Document doc) throws GMapDirectionException{
	    try {
	        NodeList nl1 = doc.getElementsByTagName("duration");
	        Node node1 = nl1.item(0);
	        NodeList nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
	        Log.i("DurationValue", node2.getTextContent());
	        return Integer.parseInt(node2.getTextContent());
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	    }
	}
	
	public String getDistanceText(Document doc) throws GMapDirectionException{
	    /*
	     * while (en.hasMoreElements()) { type type = (type) en.nextElement();
	     * 
	     * }
	     */
	
	    try {
	        NodeList nl1;
	        nl1 = doc.getElementsByTagName("distance");
	
	        Node node1 = nl1.item(nl1.getLength() - 1);
	        NodeList nl2 = null;
	        nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
	        Log.d("DistanceText", node2.getTextContent());
	        return node2.getTextContent();
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	    }
	
	    /*
	     * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
	     * doc.getElementsByTagName("distance");
	     * 
	     * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
	     * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
	     * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
	     * node2.getTextContent()); return node2.getTextContent(); } else return
	     * "-1";} else return "-1";
	     */
	}
	
	public int getDistanceValue(Document doc) throws GMapDirectionException{
	    try {
	        NodeList nl1 = doc.getElementsByTagName("distance");
	        Node node1 = null;
	        node1 = nl1.item(nl1.getLength() - 1);
	        NodeList nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
	        Log.i("DistanceValue", node2.getTextContent());
	        return Integer.parseInt(node2.getTextContent());
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	    }
	    /*
	     * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
	     * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
	     * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
	     * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
	     * node2.getTextContent()); return
	     * Integer.parseInt(node2.getTextContent()); } else return 0;
	     */
	}
	
	public String getStartAddress(Document doc) throws GMapDirectionException{
	    try {
	        NodeList nl1 = doc.getElementsByTagName("start_address");
	        Node node1 = nl1.item(0);
	        Log.i("StartAddress", node1.getTextContent());
	        return node1.getTextContent();
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	    }
	
	}
	
	public String getEndAddress(Document doc) throws GMapDirectionException {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("end_address");
	        Node node1 = nl1.item(0);
	        Log.i("StartAddress", node1.getTextContent());
	        return node1.getTextContent();
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	}
	}
	public String getCopyRights(Document doc) throws GMapDirectionException {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("copyrights");
	        Node node1 = nl1.item(0);
	        Log.i("CopyRights", node1.getTextContent());
	        return node1.getTextContent();
	    } catch (Exception e) {
            Log.e(GMAP_DIRECTION_TAG, " Exception: " + e.toString());
            e.printStackTrace();
            throw new GMapDirectionException();
	    }
	
	}
	
	public ArrayList<LatLng> getDirection(Document doc) {
	    NodeList nl1, nl2, nl3;
	    ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
	    nl1 = doc.getElementsByTagName("step");
	    if (nl1.getLength() > 0) {
	        for (int i = 0; i < nl1.getLength(); i++) {
	            Node node1 = nl1.item(i);
	            nl2 = node1.getChildNodes();
	
	            Node locationNode = nl2
	                    .item(getNodeIndex(nl2, "start_location"));
	            nl3 = locationNode.getChildNodes();
	            Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            double lat = Double.parseDouble(latNode.getTextContent());
	            Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            double lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));
	
	            locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "points"));
	            ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
	            for (int j = 0; j < arr.size(); j++) {
	                listGeopoints.add(new LatLng(arr.get(j).latitude, arr
	                        .get(j).longitude));
	            }
	
	            locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            lat = Double.parseDouble(latNode.getTextContent());
	            lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));
	        }
	    }
	
	    return listGeopoints;
	}
	
	private int getNodeIndex(NodeList nl, String nodename) {
	    for (int i = 0; i < nl.getLength(); i++) {
	        if (nl.item(i).getNodeName().equals(nodename))
	            return i;
	    }
	    return -1;
	}
	
	private ArrayList<LatLng> decodePoly(String encoded) {
	    ArrayList<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;
	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;
	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;
	
	        LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	        poly.add(position);
	    }
	    return poly;
	}
}