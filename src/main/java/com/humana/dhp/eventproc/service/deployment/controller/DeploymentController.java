package com.humana.dhp.eventproc.service.deployment.controller;

import com.nimbusds.jose.util.JSONStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class DeploymentController {

    @Autowired
    WebClient webClient;

    //    @PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasAuthority('SCOPE_Obo.Deployment.NifiScope')")
    @PostMapping("/v1/deployment")
    public ResponseEntity<String> createDeployment(
            @RegisteredOAuth2AuthorizedClient("dhp-eventproc-nifi-agent") OAuth2AuthorizedClient nifiService,
            @RequestBody Object body
    ) {

        return new ResponseEntity<>(callNifiAgent(nifiService, body) + body.toString(), HttpStatus.OK);
    }

    private String callNifiAgent(OAuth2AuthorizedClient nifiService, Object reqBody) {
        if (null != nifiService) {
            System.out.println(nifiService.getAccessToken().getTokenValue());

            String body = this.webClient
                    .post()
                    .uri("http://localhost:8383/v1/nifi-agent")
                    .accept(MediaType.ALL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", String.format("Bearer %s", nifiService.getAccessToken().getTokenValue()))
                    .body(BodyInserters.fromValue(JSONStringUtils.toJSONString(reqBody.toString())))
                    .attributes(oauth2AuthorizedClient(nifiService))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return "/v1/deployment -> " + (null != body ? body : "null");
        } else {
            return "/v1/deployment -> failed.";
        }
    }
}
