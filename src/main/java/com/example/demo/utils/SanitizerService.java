package com.example.demo.utils;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Service
public class SanitizerService {

    private static final PolicyFactory HTML_POLICY = new HtmlPolicyBuilder()
            .allowElements("b" , "i" , "u" , "strong" , "em" , "p" , "br")
            .allowElements("a")
            .allowUrlProtocols("http" , "https" , "mailto")
            .allowAttributes("href").onElements("a")
            .toFactory();

    public String sanitize(String rawInput) {
        if(rawInput == null){
            return null;
        }

        return HTML_POLICY.sanitize(rawInput);
    }

}
