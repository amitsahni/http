### Global Configuration
Java
```
        ApiConfiguration.INSTANCE
                        .baseUrl(MainActivityModel.Companion.getENDPOINT_BASE())
                        .timeOut(1000L, 2000L)
                        .logging(true)
                        .config();
```
Kotlin
```kotlin
ApiConfiguration
                .baseUrl("baseurl")
                .logging(true)
                .timeOut(connectTimeOut, readTimeOut)
                .config()
```

-----

### Api calling using Webconnect
Java
```
       WebConnect.INSTANCE.with(MainActivityModel.Companion.getENDPOINT_GET())
                       .get()
                       .success(ResponseModel.class, model -> {
                           Log.d("TAG", "Model = " + model.toString());
                           return Unit.INSTANCE;
                       })
                       .analyticsListener((time, byteSent, byteReceived, isCache) -> {
                           Log.d("TAG", "time = " + time);
                           return Unit.INSTANCE;
                       })
                       .response(s -> {
                           Log.d(getLocalClassName(), "Response = " + s);
                           return Unit.INSTANCE;
                       })
                       .connect();
 ```
 Kotlin
 ```kotlin
WebConnect.with("url")
                .get()   // .put() .post() .delete()
                .queryParam(headerMap)
                .headerParam(headerMap)
                .timeOut(100L, 50L)
                .loader {
                    
                }
                .success(ResponseModel::class.java) {
                    
                }
                .error(Error::class.java) {
                    
                }
                .failure { model, msg ->
                    
                }
                .connect()
```

#### Multipart
```kotlin
 WebConnect.with("url")
                 .multipart()
                 .post() // .put() .patch()
                 .queryParam(headerMap)
                 .headerParam(headerMap)
                 .multipartParam(param)
                 .multipartParamFile(fileParam,context)
                 .timeOut(100L, 50L)
                 .loader {
                     
                 }
                 .success(ResponseModel::class.java) {
                     
                 }
                 .error(Error::class.java) {
                     
                 }
                 .failure { model, msg ->
                     
                 }
                 .connect()

```
If you have multiple part files against single key then use 
.multipartParamFileList(requestFile)
#### Following method will only allowed for POST, PUT, DELETE, PATCH

- bodyParam() - Used to send data in body as raw json
- formDataParam() - Used to send data as form-data
 
-----
#### Download File/Image (Anything)
```groovy
WebConnect.with("url")
                .download(file)
                .get() // .post() .put()
                .success { 
                    
                }
                .error(Error::class.java){
                    
                }
                .connect()
```

Download
--------
Add the JitPack repository to your root build.gradle:

```groovy
	allprojects {
		repositories {
			maven { url "https://jitpack.io" }
		}
	}
```
Add the Gradle dependency:
```groovy
	dependencies {
		compile 'com.github.amitsahni:http:2.0.0'
	}
```