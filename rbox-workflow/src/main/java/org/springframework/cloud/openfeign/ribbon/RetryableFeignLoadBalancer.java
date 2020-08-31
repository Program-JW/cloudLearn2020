//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.openfeign.ribbon;

import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.ruigu.rbox.workflow.controller.DefinitionController;
import feign.Request;
import feign.Response;
import feign.Request.Options;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRecoveryCallback;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonProperties;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.RibbonServer;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer.RibbonRequest;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer.RibbonResponse;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StreamUtils;

public class RetryableFeignLoadBalancer extends FeignLoadBalancer implements ServiceInstanceChooser {
    private static final Logger logger = LoggerFactory.getLogger(DefinitionController.class);

    private final LoadBalancedRetryFactory loadBalancedRetryFactory;

    public RetryableFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector, LoadBalancedRetryFactory loadBalancedRetryFactory) {
        super(lb, clientConfig, serverIntrospector);
        this.loadBalancedRetryFactory = loadBalancedRetryFactory;
        this.setRetryHandler(new DefaultLoadBalancerRetryHandler(clientConfig));
    }

    @Override
    public RibbonResponse execute(final RibbonRequest request, IClientConfig configOverride) throws IOException {
        final Options options;
        if (configOverride != null) {
            RibbonProperties ribbon = RibbonProperties.from(configOverride);
            options = new Options(ribbon.connectTimeout(this.connectTimeout), ribbon.readTimeout(this.readTimeout));
        } else {
            options = new Options(this.connectTimeout, this.readTimeout);
        }

        final LoadBalancedRetryPolicy retryPolicy = this.loadBalancedRetryFactory.createRetryPolicy(this.getClientName(), this);
        RetryTemplate retryTemplate = new RetryTemplate();
        BackOffPolicy backOffPolicy = this.loadBalancedRetryFactory.createBackOffPolicy(this.getClientName());
        retryTemplate.setBackOffPolicy((BackOffPolicy)(backOffPolicy == null ? new NoBackOffPolicy() : backOffPolicy));
        RetryListener[] retryListeners = this.loadBalancedRetryFactory.createRetryListeners(this.getClientName());
        if (retryListeners != null && retryListeners.length != 0) {
            retryTemplate.setListeners(retryListeners);
        }

        retryTemplate.setRetryPolicy((RetryPolicy)(retryPolicy == null ? new NeverRetryPolicy() : new FeignRetryPolicy(request.toHttpRequest(), retryPolicy, this, this.getClientName())));
        return (RibbonResponse)retryTemplate.execute(new RetryCallback<RibbonResponse, IOException>() {
            @Override
            public RibbonResponse doWithRetry(RetryContext retryContext) throws IOException {
                Request feignRequest = null;
                if (retryContext instanceof LoadBalancedRetryContext) {
                    ServiceInstance service = ((LoadBalancedRetryContext)retryContext).getServiceInstance();
                    if (service != null) {
                        feignRequest = ((RibbonRequest)request.replaceUri(RetryableFeignLoadBalancer.this.reconstructURIWithServer(new Server(service.getHost(), service.getPort()), request.getUri()))).toRequest();
                    }
                }

                logger.debug("您使用feign调用了" + feignRequest.url());

                if (feignRequest == null) {
                    feignRequest = request.toRequest();
                }

                Response response = request.client().execute(feignRequest, options);
                if (retryPolicy != null && retryPolicy.retryableStatusCode(response.status())) {
                    byte[] byteArray = response.body() == null ? new byte[0] : StreamUtils.copyToByteArray(response.body().asInputStream());
                    response.close();
                    throw new RibbonResponseStatusCodeException(RetryableFeignLoadBalancer.this.clientName, response, byteArray, request.getUri());
                } else {
                    return new RibbonResponse(request.getUri(), response);
                }
            }
        }, new LoadBalancedRecoveryCallback<RibbonResponse, Response>() {
            protected RibbonResponse createResponse(Response response, URI uri) {
                return new RibbonResponse(uri, response);
            }
        });
    }

    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(RibbonRequest request, IClientConfig requestConfig) {
        return new RequestSpecificRetryHandler(false, false, this.getRetryHandler(), requestConfig);
    }

    public ServiceInstance choose(String serviceId) {
        return new RibbonServer(serviceId, this.getLoadBalancer().chooseServer(serviceId));
    }
}
