package com.crm.main.feign;

import com.crm.sharedlib.dto.request.CreateDefaultFileRequest;
import com.crm.sharedlib.dto.response.FileIdResponse;
import com.crm.sharedlib.feign.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "file-service", configuration = FeignClientConfig.class)
public interface FileClient {

    @PostMapping("/api/internal/files/default")
    FileIdResponse createDefaultDirectory(@RequestBody CreateDefaultFileRequest request);

}
