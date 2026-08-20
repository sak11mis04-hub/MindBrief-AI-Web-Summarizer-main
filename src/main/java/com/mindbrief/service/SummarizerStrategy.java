package com.mindbrief.service;

public interface SummarizerStrategy {
    String summarize(String text) throws Exception;
    String getName();
}
