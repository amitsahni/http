#1.0.7
* Added QueryMap for allowing duplicate keys in QueryParameters
* Removed RxAndroid 
* Added 3 new callback success, error, failure
  1. success - Any success event captured here
  2. error - Any error event captured here
  3. failure - Any other type of error (network,timeouts) captured here
* Added Live Data for success, error, failure is need everything in same place
  1. SuccessLiveData
  2. ErrorLiveData
  3. FailureLiveData