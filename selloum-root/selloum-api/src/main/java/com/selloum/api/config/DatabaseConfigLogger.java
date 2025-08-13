package com.selloum.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseConfigLogger {

	private static final Logger log = LoggerFactory.getLogger(DatabaseConfigLogger.class);
    private final Environment env;
    public DatabaseConfigLogger(Environment env) { this.env = env; }

    @PostConstruct
    public void dump() {
        String raw = env.getProperty("DB_URL");                  // .env 키
        String yml = env.getProperty("spring.datasource.url");   // yml 치환 결과
        log.info("DB_URL raw -> [{}]", raw);
        log.info("spring.datasource.url -> [{}]", yml);
    }
}

