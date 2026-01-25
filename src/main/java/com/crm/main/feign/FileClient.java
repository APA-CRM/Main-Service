package com.crm.main.feign;

import com.crm.sharedlib.core.dto.request.CreateDefaultFileRequest;
import com.crm.sharedlib.core.dto.response.FileIdResponse;
import com.crm.sharedlib.core.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${app.clients.auth-service.name}", configuration = FeignClientConfig.class)
public interface FileClient {

    @PostMapping("/api/internal/files/default")
    FileIdResponse createDefaultDirectory(@RequestBody CreateDefaultFileRequest request);

}
