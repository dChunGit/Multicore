AbortLock: Restore pre-critical section thread state  

Setup:  
	Your library classpath must include the Google gson jar regardless of IDE choice. 
   
	If using Intellij (which is what we used), make sure project sdk is set correctly for your project after importing. The Google gson jar is included in the lib folder and can be added through maven via the Project Structure/Libraries/+/"From maven. In the popup dialog box, search gson and select com.google.code.gson:2.8.5.  

Test:  
	All tests are found in the AbortLockTest class. They are junit tests so run them accordingly (make sure to add junit to your classpath if you have not done so already).  

	The tests with Variable in their name may take up to 4 seconds to run as they each are aggregating an average of 10 test runs for 30 different thread parameters.
