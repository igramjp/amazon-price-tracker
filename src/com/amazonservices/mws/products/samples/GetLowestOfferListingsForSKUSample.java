/*******************************************************************************
 * Copyright 2009-2016 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * Marketplace Web Service Products
 * API Version: 2011-10-01
 * Library Version: 2016-06-01
 * Generated: Mon Jun 13 10:07:47 PDT 2016
 */
package com.amazonservices.mws.products.samples;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amazonservices.mws.products.MarketplaceWebServiceProducts;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.GetLowestOfferListingsForSKURequest;
import com.amazonservices.mws.products.model.GetLowestOfferListingsForSKUResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;
import com.amazonservices.mws.products.model.SellerSKUListType;

import amazon.Construct;


/** Sample call for GetLowestOfferListingsForSKU. */
public class GetLowestOfferListingsForSKUSample {

    /**
     * Call the service, log response and exceptions.
     *
     * @param client
     * @param request
     *
     * @return The response.
     */
    public static List<Integer> invokeGetLowestOfferListingsForSKU(
            MarketplaceWebServiceProducts client,
            GetLowestOfferListingsForSKURequest request) {
        try {
            // Call the service.
            GetLowestOfferListingsForSKUResponse response = client.getLowestOfferListingsForSKU(request);
            // ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            // System.out.println("Response:");
            // System.out.println("RequestId: "+rhmd.getRequestId());
            // System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            // System.out.println(responseXml);

            // Read XML
            Element emp = null;
            Element empPrice = null;
            Element empPoint = null;
        	NodeList priceElements = null;
        	NodeList pointElements = null;
        	List<String> listingPriceList = new ArrayList<String>();
        	List<Integer> lowestPriceList = new ArrayList<Integer>();
        	List<String> listingPointList = new ArrayList<String>();
        	List<Integer> lowestPointList = new ArrayList<Integer>();
            try {
            	DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            	InputSource inputSource = new InputSource();
            	inputSource.setCharacterStream(new StringReader(responseXml));
            	Document document = documentBuilder.parse(inputSource);

            	Element rootElement = document.getDocumentElement();
            	NodeList empNodes = rootElement.getElementsByTagName("Price");

            	for(int i = 0; i < empNodes.getLength(); i++){
            		emp = (Element)empNodes.item(i);
            		priceElements = emp.getElementsByTagName("ListingPrice");
            		empPrice = (Element)priceElements.item(0);
            		priceElements = empPrice.getElementsByTagName("Amount");

            		pointElements = emp.getElementsByTagName("Points");
            		empPoint = (Element)pointElements.item(0);

            		if(priceElements.getLength() > 0) {
            			listingPriceList.add(priceElements.item(0).getFirstChild().getNodeValue());
            			if(empPoint == null){
            				listingPointList.add("0");
            			}else{
            				pointElements = empPoint.getElementsByTagName("Amount");
            				listingPointList.add(pointElements.item(0).getFirstChild().getNodeValue());
            			}
            		}
            	}


            } catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
            /*
            for(int j=0; j < listingPriceList.size(); j++) {
            	if(currentPrice == (int)Double.parseDouble(listingPriceList.get(j))) {
            		listingPriceList.remove(j);
            		listingPointList.remove(j);
            		break;
            	}
            }
            */

            for(int k=0; k < listingPriceList.size(); k++) {
            	lowestPriceList.add((int)Double.parseDouble(listingPriceList.get(k)));
            	lowestPointList.add((int)Double.parseDouble(listingPointList.get(k)));
            	System.out.println("LowestPrice" + (k+1) + " : " + (int)Double.parseDouble(listingPriceList.get(k)));
            	System.out.println("LowestPoint" + (k+1) + " : " + (int)Double.parseDouble(listingPointList.get(k)));
            }

            List<Integer> lowestList = new ArrayList<Integer>();
            lowestList.addAll(lowestPriceList);
            lowestList.addAll(lowestPointList);
            return lowestList;
        } catch (MarketplaceWebServiceProductsException ex) {
            // Exception properties are important for diagnostics.
            System.out.println("Service Exception:");
            ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
            if(rhmd != null) {
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
            }
            System.out.println("Message: "+ex.getMessage());
            System.out.println("StatusCode: "+ex.getStatusCode());
            System.out.println("ErrorCode: "+ex.getErrorCode());
            System.out.println("ErrorType: "+ex.getErrorType());
            throw ex;
        }
    }

    /**
     *  Command line entry point.
     */
    public static List<Integer> main(String sku) {

        // Get a client connection.
        // Make sure you've set the variables in MarketplaceWebServiceProductsSampleConfig.
        MarketplaceWebServiceProductsClient client = MarketplaceWebServiceProductsSampleConfig.getClient();

        // Create a request.
        GetLowestOfferListingsForSKURequest request = new GetLowestOfferListingsForSKURequest();
        String sellerId = Construct.SELLER_ID;
        request.setSellerId(sellerId);
        String marketplaceId = Construct.MARKETPLACE_ID;
        request.setMarketplaceId(marketplaceId);
        SellerSKUListType sellerSKUList = new SellerSKUListType();
        List<String> sellerSKU = new ArrayList<String>();
        sellerSKU.add(sku);
        sellerSKUList.setSellerSKU(sellerSKU);
        request.setSellerSKUList(sellerSKUList);
        String itemCondition = Construct.ITEM_CONDITION;
        request.setItemCondition(itemCondition);
        Boolean excludeMe = Construct.EXCLUDE_ME;
        request.setExcludeMe(excludeMe);

        // Make the call.
        return GetLowestOfferListingsForSKUSample.invokeGetLowestOfferListingsForSKU(client, request);

    }
}
