This demo registers the servlet context Google App Engine creates in the Opscode (Chef) Platform upon startup.  It then displays the data stored in the platform regarding the current servlet container.

On this screen, you can see when the node was last updated.  The chef nodes corresponding to appengine instances have the following naming convention: applicationId-[0-9]+  

For example, here is the status page from the jclouds org in the Opscode Platform:
  http://jclouds-chef-demo.appspot.com/nodes/do

This demo uses the Google App Engine for Java SDK located at http://googleappengine.googlecode.com/files/appengine-java-sdk-1.3.5.zip

Please unzip the above file and export its location to your environment variable APPENGINE_HOME

Next, login to your opscode platform org and create a client named the same as your applicationid. Save the private key to ~/.chef/<id>.pem

Then, you can run the demo on you local machine by executing the following:
   mvn -Plive -Dappengine.applicationid=YOUR_APPLICATIONID -Djclouds.opscodeplatform.org=YOUR_ORG_IN_OPSCODE_PLATFORM clean install

When the above runs successfully on your machine, you can deploy to google appengine via the following:

    $APPENGINE_HOME/bin/appcfg.sh -e YOUR_EMAIL update target/jclouds-chef-demo-googleappengine

Once this completes, you can view your status via:
  http://YOUR_GOOGLE_APPENGINE_ID.appspot.com/nodes/do

Note that if you are using the Opscode Platform, 5 nodes are free, so be sure to watch out and delete excess nodes in the console as needed:
   https://manage.opscode.com/nodes
   
