package webconnect.com.webconnect;

/**
 * Created by clickapps on 27/12/17.
 */

public class BuilderRequest extends Request<BuilderRequest> {

    public BuilderRequest(GetRequestBuilder builder) {
        super(builder);
    }

    public BuilderRequest(PostRequestBuilder builder) {
        super(builder);
    }

    public BuilderRequest(DownloadBuilder builder) {
        super(builder);
    }

    public BuilderRequest(MultiPartBuilder builder) {
        super(builder);
    }

    public static class GetRequestBuilder extends Request.GetRequestBuilder<GetRequestBuilder> {

        public GetRequestBuilder(WebParam param) {
            super(param);
        }

        public BuilderRequest build() {
            return new BuilderRequest(this);
        }
    }

    public static class HeadRequestBuilder extends GetRequestBuilder {

        public HeadRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class OptionsRequestBuilder extends GetRequestBuilder {

        public OptionsRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class PostRequestBuilder extends Request.PostRequestBuilder<PostRequestBuilder> {

        public PostRequestBuilder(WebParam param) {
            super(param);
        }

        public BuilderRequest build() {
            return new BuilderRequest(this);
        }
    }

    public static class PutRequestBuilder extends PostRequestBuilder {

        public PutRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class DeleteRequestBuilder extends PostRequestBuilder {

        public DeleteRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class PatchRequestBuilder extends PostRequestBuilder {

        public PatchRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class DownloadBuilder extends Request.DownloadBuilder<DownloadBuilder> {

        public DownloadBuilder(WebParam param) {
            super(param);
        }

        public BuilderRequest build() {
            return new BuilderRequest(this);
        }
    }

    public static class MultiPartBuilder extends Request.MultiPartBuilder<MultiPartBuilder> {

        public MultiPartBuilder(WebParam param) {
            super(param);
        }

        public BuilderRequest build() {
            return new BuilderRequest(this);
        }
    }


}
