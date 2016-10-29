package cache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import sample.data.jpa.CacheTestApplication;
import sample.data.jpa.domain.City;
import sample.data.jpa.domain.Hotel;
import sample.data.jpa.domain.Review;
import sample.data.jpa.domain.ReviewDetails;
import sample.data.jpa.service.CityRepository;
import sample.data.jpa.service.HotelRepository;
import sample.data.jpa.service.NativeCityRepository;
import sample.data.jpa.service.ReviewRepository;

import javax.management.MalformedObjectNameException;
import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import static cache.EhcacheJmxClient.getCityHits;
import static cache.EhcacheJmxClient.getCityMisses;
import static cache.EhcacheJmxClient.getHotelHits;
import static cache.EhcacheJmxClient.getHotelMisses;
import static cache.ExpectedCacheStatistics.*;
import static cache.ExpectedCacheStatistics.plusOneCityHits;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CacheTestApplication.class)
@WebIntegrationTest
@Transactional
public class CacheTests {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    NativeCityRepository nativeCityRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EntityManager em;

    private Cache cityCache;
    private Cache hotelCache;

    @Before
    public void setup() throws IOException, MalformedObjectNameException {
        cityCache = this.cacheManager.getCache("city");
        hotelCache = this.cacheManager.getCache("hotel");

        EhcacheJmxClient.connectJmx();

        cityCache.clear();
        hotelCache.clear();
    }

    @Test
    public void testCachable() {
        City city = cityRepository.findOne(1L);
        assertThat(getCityHits(), is(sameCityHits()));
        assertThat(getCityMisses(), is(plusOneCityMisses()));

        em.detach(city);
        city = cityRepository.findOne(1L);

        assertThat(getCityHits(), is(plusOneCityHits()));
        assertThat(getCityMisses(), is(sameCityMisses()));

        em.detach(city);
        city = cityRepository.findOne(1L);

        assertThat(getCityHits(), is(plusOneCityHits()));
        assertThat(getCityMisses(), is(sameCityMisses()));

        em.detach(city);
        cityRepository.findOne(2L);

        assertThat(getCityHits(), is(sameCityHits()));
        assertThat(getCityMisses(), is(plusOneCityMisses()));

        assertThat(getCachedCity(1L), is(notNullValue()));
        assertThat(getCachedCity(2L), is(notNullValue()));
    }

    @Test
    public void testCachePut() {
        City city = cityRepository.findOne(1L);

        assertThat(getCityHits(), is(sameCityHits()));
        assertThat(getCityMisses(), is(plusOneCityMisses()));

        City cachedCity = getCachedCity(1L);

        assertThat(cachedCity, is(notNullValue()));
        assertThat(cachedCity.getName(), is(city.getName()));

        em.detach(city);

        String newName = "newName";
        String oldName = city.getName();
        nativeCityRepository.updateNameNotCached(city, newName);

        cachedCity = getCachedCity(1L);
        assertThat(cachedCity, is(notNullValue()));
        assertThat(cachedCity.getName(), is(oldName));

        nativeCityRepository.updateNameCachePut(city, newName);

        cachedCity = getCachedCity(1L);
        assertThat(cachedCity, is(notNullValue()));
        assertThat(cachedCity.getName(), is(newName));
    }

    @Test
    public void testCacheEvict() {
        City city = cityRepository.findOne(1L);
        assertThat(getCityMisses(), is(plusOneCityMisses()));
        assertThat(EhcacheJmxClient.getCityCount(), is("1"));

        try {
            nativeCityRepository.evictFromCache(city);
        } catch (FileNotFoundException e) {

        }

        assertThat(EhcacheJmxClient.getCityCount(), is("1"));
    }

    @Test
    public void testCacheEvictBeforeInvocation() {
        City city = cityRepository.findOne(1L);
        assertThat(getCityMisses(), is(plusOneCityMisses()));
        assertThat(EhcacheJmxClient.getCityCount(), is("1"));

        try {
            nativeCityRepository.evictFromCacheBeforeInvocation(city);
        } catch (FileNotFoundException e) {

        }

        assertThat(EhcacheJmxClient.getCityCount(), is("0"));
    }

    @Test
    public void testCacheableCollection() {
        Hotel hotel = hotelRepository.findOne(2L);

        assertThat(getHotelHits(), is(sameHotelHits()));
        assertThat(getHotelMisses(), is(plusOneHotelMisses()));

        em.detach(hotel);
        Review review = reviewRepository.save(new Review(hotel, 2, new ReviewDetails()));

        assertThat(getHotelHits(), is(sameHotelHits()));
        assertThat(getHotelMisses(), is(sameHotelMisses()));

        //hotel.addReview(review);

        assertThat(getHotelHits(), is(sameHotelHits()));
        assertThat(getHotelMisses(), is(sameHotelMisses()));

        em.detach(hotel);
        hotel = hotelRepository.findOne(2L);

        assertThat(getHotelHits(), is(plusOneHotelHits()));
        assertThat(getHotelMisses(), is(sameHotelMisses()));

        Set<Review> hotelReviews = hotel.getReviews();

        assertThat(getHotelHits(), is(sameHotelHits()));
        assertThat(getHotelMisses(), is(sameHotelMisses()));

//		assertThat(hotelReviews.size(), is(5)); //todo 4 instead of 5 reviews are returned if lazy loading is performed in a separate transaction
    }

    private City getCachedCity(long id) {
        Cache.ValueWrapper object = cityCache.get(id);
        if (object == null) {
            plusOneCityMisses();
            return null;
        }
        plusOneCityHits();
        return (City) object.get();
    }


}
