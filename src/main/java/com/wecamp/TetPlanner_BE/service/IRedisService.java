package com.wecamp.TetPlanner_BE.service;

public interface IRedisService {
    void save(String key, String value, long expirationTime);
    String get(String key);
    void delete(String key);
}
