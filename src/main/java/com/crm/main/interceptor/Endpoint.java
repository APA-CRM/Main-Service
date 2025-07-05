package com.crm.main.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Endpoint {

    private HttpMethod httpMethod;

    private String uri;

}
