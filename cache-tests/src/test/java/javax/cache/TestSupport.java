/**
 *  Copyright 2011 Terracotta, Inc.
 *  Copyright 2011 Oracle, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.cache;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit test support base class
 * <p/>
 *
 * @author Yannis Cosmadopoulos
 */
class TestSupport {

    /**
     * the logger
     */
    protected final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * the default test cache name
     */
    protected static final String CACHE_NAME = "testCache";

    protected CacheConfiguration createCacheConfiguration() {
        return CacheManagerFactory.INSTANCE.createCacheConfiguration();
    }

    protected CacheManager getCacheManager() {
        return CacheManagerFactory.INSTANCE.getCacheManager();
    }

    protected CacheManager getCacheManager(String name) {
        return CacheManagerFactory.INSTANCE.getCacheManager(name);
    }

    protected <K, V> Cache<K, V> createCache() {
        return getCacheManager().
                <K, V>createCacheBuilder(CACHE_NAME).
                build();
    }

    protected <K, V> Cache<K, V> createCache(CacheLoader<K, V> cacheLoader) {
        return getCacheManager().
                <K, V>createCacheBuilder(CACHE_NAME).
                setCacheLoader(cacheLoader).
                build();
    }

    protected <K, V> Cache<K, V> createCache(CacheConfiguration config) {
        return getCacheManager().
                <K, V>createCacheBuilder(CACHE_NAME).
                setCacheConfiguration(config).
                build();
    }

    protected <A, B> Cache<A, B> createByValueCache() {
        CacheConfiguration config = createCacheConfiguration();
        config.setStoreByValue(true);
        return createCache(config);
    }

    protected <A, B> Cache<A, B> createByReferenceCache() {
        try {
            CacheConfiguration config = createCacheConfiguration();
            config.setStoreByValue(false);
            return createCache(config);
        } catch (InvalidConfigurationException e) {
            logger.log(Level.INFO, "===== cache does not support store by reference: " + e.getMessage());
            return null;
        }
    }

    protected <K, V> Cache<K, V> createOrphanCache(String name) {
        return CacheManagerFactory.INSTANCE.createCache(name);
    }

    protected LinkedHashMap<Date, Integer> createData(int count, long now) {
        LinkedHashMap<Date, Integer> map = new LinkedHashMap<Date, Integer>(count);
        for (int i = 0; i < count; i++) {
            map.put(new Date(now + i), i);
        }
        return map;
    }

    protected LinkedHashMap<Date, Integer> createData(int count) {
        return createData(count, System.currentTimeMillis());
    }

    protected <K, V> void checkGetExpectation(V expected, Cache<K, V> cache, K key) {
        if (cache.getConfiguration().isStoreByValue()) {
            assertEquals(expected, cache.get(key));
        } else {
            assertSame(expected, cache.get(key));
        }
    }
}
