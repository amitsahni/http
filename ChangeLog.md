#1.0.7
* Deprecating old callback
* Removed RxAndroid 
* Added QueryMap for allowing duplicate keys in QueryParameters
* Added 3 new callback success, error, failure
  1. success - Any success event captured here
  2. error - Any error event captured here
  3. failure - Any other type of error (network,timeouts) captured here
  4. response - Get response as string
* Added Live Data for success, error, failure is need everything in same place
  1. SuccessLiveData
  2. ErrorLiveData
  3. FailureLiveData
  
#2.0.0
* Remove context
* Remove Java compatible methods
* Add Extension function for internal use
* Added higher order function for getting result in same block
