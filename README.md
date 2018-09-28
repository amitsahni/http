### Global Configuration
```
        new ApiConfiguration.Builder(this)
                        .baseUrl(MainActivityModel.ENDPOINT_BASE)
                        .timeOut(1000L, 2000L)
                        .debug(true)
                        .config();
```

-----

### Api calling using Webconnect
```
       WebConnect.with(this.activity, ENDPOINT_GET)
                        .get()
                         .callback(new OnWebCallback() {
                         @Override
                         public void onSuccess(@Nullable Object object, int taskId) {
                         
                         }
                         @Override
                         public void onError(@Nullable Object object, String error, int taskId) {
                        
                         }
                         }).connect();
 ```
 ### HTTP POST
 ```
       Map<String, String> requestMap = new LinkedHashMap<>();
               requestMap.put("name", "Amit");
               requestMap.put("job", "manager");
               WebConnect.with(this.activity, ENDPOINT_POST)
                       .post()
                       .bodyParam(requestMap)
                       .callback(new OnWebCallback() {
                           @Override
                           public <T> void onSuccess(@Nullable T object, int taskId) {
                               if (object != null) {
                                   post.setValue(object);
                               }
                           }
       
                           @Override
                           public <T> void onError(@Nullable T object, String error, int taskId) {
                               post.setValue(object);
                           }
                       })
                       .connect();                  
                         
```
### HTTP PUT
```
Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("name", "Amit Singh");
        requestMap.put("job", "manager");
        WebConnect.with(activity, ENDPOINT_PUT)
                .put()
                .bodyParam(requestMap)
                .callback(new OnWebCallback() {
                    @Override
                    public <T> void onSuccess(@Nullable T object, int taskId) {
                        if (object != null) {
                            put.setValue(object);
                        }
                    }

                    @Override
                    public <T> void onError(@Nullable T object, String error, int taskId) {
                        put.setValue(object);
                    }
                }).connect();
```
### HTTP Delete
```
Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("name", "Amit Singh");
        requestMap.put("job", "manager");
        WebConnect.with(activity, ENDPOINT_PUT)
                .delete()
                .bodyParam(requestMap)
                .callback(new OnWebCallback() {
                    @Override
                    public <T> void onSuccess(@Nullable T object, int taskId) {
                        if (object != null) {
                            delete.setValue(object);
                        }
                    }

                    @Override
                    public <T> void onError(@Nullable T object, String error, int taskId) {
                        delete.setValue(object);
                    }
                }).connect();
```
#### Multipart
```
 Map<String, String> requestMap = new LinkedHashMap<>();
         requestMap.put("name", "Amit");
         requestMap.put("job", "manager");
         File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
         Map<String, File> requestFile = new LinkedHashMap<>();
         requestFile.put("file", file);
         WebConnect.with(activity,ENDPOINT_GET)
                 .multipart()
                 .post()
                 .multipartParam(requestMap)
                 .multipartParamFile(requestFile)
                 .callback(new OnWebCallback() {
                     @Override
                     public <T> void onSuccess(@Nullable T object, int taskId) {
                         
                     }
 
                     @Override
                     public <T> void onError(@Nullable T object, String error, int taskId) {
                        
                     }
                 }).connect();

```
#### Following method will only allowed for POST, PUT, DELETE, PATCH

- bodyParam() - Used to send data in body as raw json
- formDataParam() - Used to send data as form-data
 
-----
#### Download File/Image (Anything)
```
File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        WebConnect.with(this.activity, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg")
                .download(file)
                .get()
                .callback(new OnWebCallback() {
                    @Override
                    public <T> void onSuccess(@Nullable T object, int taskId) {
                    
                    }

                    @Override
                    public <T> void onError(@Nullable T object, String error, int taskId) {
                        
                    }
                }).connect();
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
		compile 'com.github.amitsahni:http:1.0.5'
	}
```