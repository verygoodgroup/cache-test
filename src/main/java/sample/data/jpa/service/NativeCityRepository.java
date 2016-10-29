package sample.data.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sample.data.jpa.domain.City;

import javax.persistence.EntityManager;
import java.io.FileNotFoundException;

/**
 * Created by slava on 10/29/16.
 */
@Component
@CacheConfig(cacheNames = "city")
public class NativeCityRepository {
    @Autowired
    EntityManager em;

    public City updateNameNotCached(City city, String newName) {
        return updateName(city, newName);
    }

    @CachePut(key = "#city.id")
    public City updateNameCachePut(City city, String newName) {
        return updateName(city, newName);
    }

    @CacheEvict(key = "#city.id")
    public City evictFromCache(City city) throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    @CacheEvict(key = "#city.id", beforeInvocation = true)
    public City evictFromCacheBeforeInvocation(City city) throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    private City updateName(City city, String newName) {
        em.createNativeQuery(String.format("update city set name='%s' where id = %s", newName, city.getId())).executeUpdate();
        return new City(newName, null);
    }
}
