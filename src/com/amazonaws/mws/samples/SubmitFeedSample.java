/*******************************************************************************
 *  Copyright 2009 Amazon Services.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 *  This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *  CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations under the License.
 * *****************************************************************************
 *
 *  Marketplace Web Service Java Library
 *  API Version: 2009-01-01
 *  Generated: Wed Feb 18 13:28:48 PST 2009
 *
 */

package com.amazonaws.mws.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.FeedSubmissionInfo;
import com.amazonaws.mws.model.IdList;
import com.amazonaws.mws.model.ResponseMetadata;
import com.amazonaws.mws.model.SubmitFeedRequest;
import com.amazonaws.mws.model.SubmitFeedResponse;
import com.amazonaws.mws.model.SubmitFeedResult;

import amazon.Construct;

/**
 *
 * Submit Feed Samples
 *
 *
 */
public class SubmitFeedSample {

    /**
     * Just add a few required parameters, and try the service Submit Feed
     * functionality
     *
     * @param args
     * unused
     */
    /**
     * @param args
     */
    public String action(List<String> feedSku, List<Integer> feedPrice, List<Integer> feedPoints) {

        /************************************************************************
         * Access Key ID and Secret Access Key ID, obtained from:
         * http://aws.amazon.com
         ***********************************************************************/
        final String accessKeyId = Construct.ACCESS_KEY_ID;
        final String secretAccessKey = Construct.SECRET_ACCESS_KEY;

        final String appName = Construct.APP_NAME;
        final String appVersion = Construct.APP_VERSION;

        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();

        /************************************************************************
         * Uncomment to set the appropriate MWS endpoint.
         ************************************************************************/
        // US
        // config.setServiceURL("https://mws.amazonservices.com");
        // UK
        // config.setServiceURL("https://mws.amazonservices.co.uk");
        // Germany
        // config.setServiceURL("https://mws.amazonservices.de");
        // France
        // config.setServiceURL("https://mws.amazonservices.fr");
        // Italy
        // config.setServiceURL("https://mws.amazonservices.it");
        // Japan
        config.setServiceURL("https://mws.amazonservices.jp");
        // China
        // config.setServiceURL("https://mws.amazonservices.com.cn");
        // Canada
        // config.setServiceURL("https://mws.amazonservices.ca");
        // India
        // config.setServiceURL("https://mws.amazonservices.in");

        /************************************************************************
         * You can also try advanced configuration options. Available options are:
         *
         *  - Signature Version
         *  - Proxy Host and Proxy Port
         *  - User Agent String to be sent to Marketplace Web Service
         *
         ***********************************************************************/

        /************************************************************************
         * Instantiate Http Client Implementation of Marketplace Web Service
         ***********************************************************************/

        MarketplaceWebService service = new MarketplaceWebServiceClient(
                accessKeyId, secretAccessKey, appName, appVersion, config);


        /************************************************************************
         * Setup request parameters and uncomment invoke to try out sample for
         * Submit Feed
         ***********************************************************************/

        /************************************************************************
         * Marketplace and Merchant IDs are required parameters for all
         * Marketplace Web Service calls.
         ***********************************************************************/
        final String merchantId = "A2Q8FSBIVVW4A2";
        // marketplaces to which this feed will be submitted; look at the
        // API reference document on the MWS website to see which marketplaces are
        // included if you do not specify the list yourself
        final IdList marketplaces = new IdList(Arrays.asList("A1VC38T7YXB528"));

        String contents = new String();
        contents += "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
        contents += "<AmazonEnvelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"amznenvelope.xsd\">\n";
        contents += "<Header>\n";
        contents += "<DocumentVersion>1.01</DocumentVersion>\n";
        contents += "<MerchantIdentifier>M_ROCKONCOMP_1507752</MerchantIdentifier>\n";
        contents += "</Header>\n";
        contents += "<MessageType>Price</MessageType>\n";

        for (int idx = 0; idx < feedSku.size(); idx++) {
            contents += "<Message>\n";
            contents += "<MessageID>" + (idx + 1) + "</MessageID>\n";
            contents += "<Price>\n";
            contents += "<SKU>" + feedSku.get(idx) + "</SKU>\n";
            contents += "<StandardPrice currency=\"JPY\">" + feedPrice.get(idx) + "</StandardPrice>\n";
            if(feedPoints.get(idx) > 0){
            		contents += "<StandardPricePoints>" + feedPoints.get(idx) + "</StandardPricePoints>\n";
            }
            contents += "</Price>\n";
            contents += "</Message>\n";
        }
        contents += "</AmazonEnvelope>\n";

        System.out.println(contents);

        File file = new File("feedSubmission.xml");
        FileWriter filewriter;
		try {
			filewriter = new FileWriter(file);
			filewriter.write(contents);
			filewriter.close();

			Thread.sleep(10000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setMerchant(merchantId);
        request.setMarketplaceIdList(marketplaces);
        request.setFeedType("_POST_PRODUCT_PRICING_DATA_");

        // MWS exclusively offers a streaming interface for uploading your
        // feeds. This is because
        // feed sizes can grow to the 1GB+ range - and as your business grows
        // you could otherwise
        // silently reach the feed size where your in-memory solution will no
        // longer work, leaving you
        // puzzled as to why a solution that worked for a long time suddenly
        // stopped working though
        // you made no changes. For the same reason, we strongly encourage you
        // to generate your feeds to
        // local disk then upload them directly from disk to MWS via Java -
        // without buffering them in Java
        // memory in their entirety.
        // Note: MarketplaceWebServiceClient will not retry a submit feed request
        // because there is no way to reset the InputStream from our client.
        // To enable retry, recreate the InputStream and resubmit the feed
        // with the new InputStream.
        //
        FileInputStream xml;
        try {
			xml = new FileInputStream("feedSubmission.xml");
			request.setFeedContent(xml);
			request.setContentMD5(computeContentMD5HeaderValue(xml));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        return invokeSubmitFeed(service, request);
    }

    /**
     * Submit Feed request sample Uploads a file for processing together with
     * the necessary metadata to process the file, such as which type of feed it
     * is. PurgeAndReplace if true means that your existing e.g. inventory is
     * wiped out and replace with the contents of this feed - use with caution
     * (the default is false).
     *
     * @param service
     *            instance of MarketplaceWebService service
     * @param request
     *            Action to invoke
     */
    public String invokeSubmitFeed(MarketplaceWebService service, SubmitFeedRequest request) {
    	SubmitFeedResponse response           = null;
    	SubmitFeedResult submitFeedResult     = null;
    	FeedSubmissionInfo feedSubmissionInfo = null;
        try {
            response = service.submitFeed(request);

            System.out.println("SubmitFeed Action Response");
            System.out.println("=====================================");
            System.out.println();

            System.out.println("    SubmitFeedResponse");
            if (response.isSetSubmitFeedResult()) {
                System.out.println("        SubmitFeedResult");
                submitFeedResult = response.getSubmitFeedResult();
                if (submitFeedResult.isSetFeedSubmissionInfo()) {
                    System.out.println("            FeedSubmissionInfo");
                    feedSubmissionInfo = submitFeedResult.getFeedSubmissionInfo();
                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                        System.out.println("                FeedSubmissionId");
                        System.out.println("                    " + feedSubmissionInfo.getFeedSubmissionId());
                    }
                    if (feedSubmissionInfo.isSetFeedType()) {
                        System.out.println("                FeedType");
                        System.out.println("                    " + feedSubmissionInfo.getFeedType());
                    }
                    if (feedSubmissionInfo.isSetSubmittedDate()) {
                        System.out.println("                SubmittedDate");
                        System.out.println("                    " + feedSubmissionInfo.getSubmittedDate());
                    }
                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                        System.out.println("                FeedProcessingStatus");
                        System.out.println("                    " + feedSubmissionInfo.getFeedProcessingStatus());
                    }
                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
                        System.out.println("                StartedProcessingDate");
                        System.out.println("                    " + feedSubmissionInfo.getStartedProcessingDate());
                        System.out.println();
                    }
                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
                        System.out.println("                CompletedProcessingDate");
                        System.out.println("                    " + feedSubmissionInfo.getCompletedProcessingDate());
                    }
                }
            }
            if (response.isSetResponseMetadata()) {
                System.out.println("        ResponseMetadata");
                ResponseMetadata responseMetadata = response.getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    System.out.println("            RequestId");
                    System.out.println("                " + responseMetadata.getRequestId());
                }
            }
            System.out.println(response.getResponseHeaderMetadata());
            System.out.println();
            System.out.println();

        } catch (MarketplaceWebServiceException ex) {

            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
        if (feedSubmissionInfo != null) {
        	return feedSubmissionInfo.getFeedSubmissionId();
        } else {
        	return null;
        }
    }

    /**
    * Calculate content MD5 header values for feeds stored on disk.
    */
    public static String computeContentMD5HeaderValue(FileInputStream fis) throws IOException, NoSuchAlgorithmException {
    	DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("MD5"));
    	byte[] buffer = new byte[8192];
    	while( dis.read( buffer ) > 0 );
    	String md5Content = new String(org.apache.commons.codec.binary.Base64.encodeBase64(dis.getMessageDigest().digest()));
    	// Effectively resets the stream to be beginning of the file via a FileChannel.
    	fis.getChannel().position( 0 );
    	return md5Content;
    }
}
