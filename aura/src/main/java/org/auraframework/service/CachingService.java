/*
 * Copyright (C) 2013 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.auraframework.service;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import org.auraframework.builder.CacheBuilder;
import org.auraframework.cache.Cache;
import org.auraframework.def.DefDescriptor;
import org.auraframework.def.Definition;
import org.auraframework.system.DependencyEntry;
import org.auraframework.system.RegistrySet;
import org.auraframework.system.SourceListener;
import org.auraframework.system.SourceListener.SourceMonitorEvent;

import com.google.common.base.Optional;

/**
 * <p>
 * Service for constructing an instance of a Cache
 * </p>
 */
public interface CachingService {

    /**
     * Provided a builder object that can create a cache with key K and type T
     *
     * @param K - the type of keys stored in this cache
     * @param T - the type of objects stored in this cache
     *
     * @return - A new cacheBuilder whose build method can build a new cache.
     */
    <K, T> CacheBuilder<K, T> getCacheBuilder();

    Cache<DefDescriptor<?>, Boolean> getExistsCache();

    Cache<DefDescriptor<?>, Optional<? extends Definition>> getDefsCache();

    Cache<String, String> getStringsCache();

    Cache<String, String> getAltStringsCache();
    
    /**
     * Gets the cache for css strings, e.g., app.css.
     * <p>
     * This allows for core to override how app.css is cached. This separation from the main strings cache can be
     * removed once core is ready override the cache for all strings.
     */
    default Cache<String, String> getCssStringsCache() {
        return null; // FIXME remove default method...
    }

    Cache<String, Set<DefDescriptor<?>>> getDescriptorFilterCache();

    Cache<String, DependencyEntry> getDepsCache();

    Cache<String, String> getClientLibraryOutputCache();

    Cache<RegistrySet.RegistrySetKey, RegistrySet> getRegistrySetCache();
    
    Lock getReadLock();

    Lock getWriteLock();

    void notifyDependentSourceChange(
            Collection<WeakReference<SourceListener>> listeners,
            SourceMonitorEvent event, String filePath);
}
