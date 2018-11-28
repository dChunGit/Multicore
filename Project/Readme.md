AbortLock: Restore pre-critical section thread state  

Github: https://github.com/dChunGit/Multicore/tree/master/Project

Setup:  
	Your library classpath must include the Google gson jar regardless of IDE choice. 
   
	If using IntelliJ (which is what we used), make sure Project SDK is set correctly for your project after importing. The Google gson jar is included in the lib folder and can be added through Maven via the Project Structure/Libraries/+/"From Maven". In the popup dialog box, search gson and select com.google.code.gson:2.8.5.  

Test:  
	All tests are found in the AbortLockTest class. They are JUNIT tests so run them accordingly (make sure to add JUNIT to your classpath if you have not done so already).  

	The tests with "Variable" in their name may take up to 4 seconds to run as they each are aggregating an average of 10 test runs for 30 different thread parameters.
