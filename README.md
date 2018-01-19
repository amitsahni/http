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
                            public <T> void onSuccess(@Nullable T object, int taskId, Response response) {
                                
                            }
        
                            @Override
                            public <T> void onError(@Nullable T object, String error, int taskId) {
                               
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
                 .multipartParam(requestMap)
                 .multipartParamFile(requestFile)
                 .callback(new OnWebCallback() {
                     @Override
                     public <T> void onSuccess(@Nullable T object, int taskId, Response response) {
                         
                     }
 
                     @Override
                     public <T> void onError(@Nullable T object, String error, int taskId) {
                        
                     }
                 }).connect();

```
-----
#### Download File/Image (Anything)
```
File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        WebConnect.with(this.activity, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg")
                .download(file)
                .callback(new OnWebCallback() {
                    @Override
                    public <T> void onSuccess(@Nullable T object, int taskId, Response response) {
                    
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
		compile 'com.github.amitsahni:http:latest'
	}
```