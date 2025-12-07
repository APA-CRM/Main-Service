package com.crm.main.service.wrapper;

import com.crm.main.feign.FileClient;
import com.crm.sharedlib.core.dto.request.CreateDefaultFileRequest;
import com.crm.sharedlib.core.dto.response.FileIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileClientWrapper {

    private final FileClient fileClient;

    public UUID createDefaultDirectory(String name) {
        CreateDefaultFileRequest request = new CreateDefaultFileRequest(name);

        FileIdResponse response = fileClient.createDefaultDirectory(request);

        return response.getId();
    }

}
