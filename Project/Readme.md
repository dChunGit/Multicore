AbortLock: Restore pre-critical section thread state  

Setup:  
	Your classpath must include the Google gson jar regardless of IDE choice.  
	If using Intellij (which is what the files in this repo are), make sure project sdk is set correctly for your project after importing. The Google gson jar can be obtained through maven via the Project Structure/Libraries/+/<search gson and select com.google.code.gson:2.8.5.  

Test:  
	All tests are found in the AbortLockTest class. They are junit tests so run them accordingly (make sure to add junit to your classpath if you have not done so already).