package webconnect.com.webconnect;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by clickapps on 28/12/17.
 */

public class HTTPInternalNetworking {

    public static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final WebParam webParam;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, WebParam webParam) {
            this.responseBody = responseBody;
            this.webParam = webParam;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    if (webParam.progressListener != null)
                        webParam.progressListener.update(totalBytesRead, responseBody.contentLength());
                    return bytesRead;
                }
            };
        }
    }
}
