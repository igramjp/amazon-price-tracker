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
import java.util.Arrays;

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
import com.amazonservices.mws.products.model.GetMyPriceForSKURequest;
import com.amazonservices.mws.products.model.GetMyPriceForSKUResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;
import com.amazonservices.mws.products.model.SellerSKUListType;

import amazon.Construct;


/** Sample call for GetMyPriceForSKU. */
public class GetMyPriceForSKUSample {

    /**
     * Call the service, log response and exceptions.
     *
     * @param client
     * @param request
     *
     * @return The response.
     */
    public static Integer invokeGetMyPriceForSKU(
            MarketplaceWebServiceProducts client,
            GetMyPriceForSKURequest request) {
        try {
            // Call the service.
            GetMyPriceForSKUResponse response = client.getMyPriceForSKU(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            // System.out.println("Response:");
            // System.out.println("RequestId: "+rhmd.getRequestId());
            // System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            // System.out.println(responseXml);
            // Read XML
        	Element emp = null;
        	NodeList nameElements = null;
            try {
            	DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            	InputSource inputSource = new InputSource();
            	inputSource.setCharacterStream(new StringReader(responseXml));
            	Document document = documentBuilder.parse(inputSource);

            	Element rootElement = document.getDocumentElement();
            	NodeList empNodes = rootElement.getElementsByTagName("RegularPrice");
            	for(int i = 0; i < empNodes.getLength(); i++) {
            		emp = (Element)empNodes.item(i);
            		nameElements = emp.getElementsByTagName("Amount");
            		if(nameElements.getLength()>0){
            			// System.out.println("RegularPrice: " +nameElements.item(0).getFirstChild().getNodeValue());
            		}
            	}
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
            if(nameElements != null){
            	return (int)Double.parseDouble(nameElements.item(0).getFirstChild().getNodeValue());
            } else{
            	return null;
            }
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
    public static Integer main(String sku) {

        // Get a client connection.
        // Make sure you've set the variables in MarketplaceWebServiceProductsSampleConfig.
        MarketplaceWebServiceProductsClient client = MarketplaceWebServiceProductsSampleConfig.getClient();

        // Create a request.
        GetMyPriceForSKURequest request = new GetMyPriceForSKURequest();
        String sellerId = Construct.SELLER_ID;
        request.setSellerId(sellerId);
        String marketplaceId = Construct.MARKETPLACE_ID;
        request.setMarketplaceId(marketplaceId);
        SellerSKUListType sellerSKUList = new SellerSKUListType();
        sellerSKUList.setSellerSKU(Arrays.asList(sku));
        request.setSellerSKUList(sellerSKUList);

        // Make the call.
        return GetMyPriceForSKUSample.invokeGetMyPriceForSKU(client, request);
    }

}
