package com.vino.config.analyzer;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class NullPointFailureAnalyzer extends AbstractFailureAnalyzer<NullPointerException> {

    public static void main(String[] args) {
        System.out.println(String.valueOf(10).contains("1"));
    }

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, NullPointerException cause) {
        return new FailureAnalysis(cause.getMessage(), "请检查空指针", cause);
    }
}

