package awsapi;

import com.amazonaws.opensdk.config.ConnectionConfiguration;
import com.amazonaws.opensdk.config.TimeoutConfiguration;
import com.hollingsworth.test.sdk.model.*;
import com.hollingsworth.test.*;
import com.hollingsworth.test.sdk.test_sdk.*;

public class SDKClient {
    public static com.hollingsworth.test.sdk.test_sdk test_sdk;

    public SDKClient(){
        initSdk();
    }
    private void initSdk() {
        test_sdk = com.hollingsworth.test.sdk.test_sdk.builder()
                .connectionConfiguration(
                        new ConnectionConfiguration()
                                .maxConnections(100)
                                .connectionMaxIdleMillis(1000))
                .timeoutConfiguration(
                        new TimeoutConfiguration()
                                .httpRequestTimeout(3000)
                                .totalExecutionTimeout(10000)
                                .socketTimeout(2000))
                .build();

    }
    public String getResultWithQueryParameters(String a, String b, String op) {
        GetMyidMyidResult rootResult = test_sdk.getMyidMyid(new GetMyidMyidRequest().myid("1"));
        return rootResult.toString();
    }
    // Calling shutdown is not necessary unless you want to exert explicit control of this resource.
    public void shutdown() {
        test_sdk.shutdown();
    }
}
