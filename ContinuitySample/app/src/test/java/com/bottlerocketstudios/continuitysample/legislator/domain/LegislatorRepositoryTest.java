package com.bottlerocketstudios.continuitysample.legislator.domain;

import android.location.Location;

import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.util.LogIt;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;
import com.bottlerocketstudios.continuitysample.legislator.usecase.GetLegislatorsByZipAgent;
import com.bottlerocketstudios.continuitysample.util.ResultContainer;
import com.bottlerocketstudios.continuitysample.util.SafeWait;
import com.bottlerocketstudios.continuitysample.util.StubbedLruCache;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.FunctionalAgentListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;

import static org.mockito.Mockito.mock;

/**
 * Created on 9/14/16.
 */
public class LegislatorRepositoryTest {

    private LegislatorRepository mLegislatorRepository;

    @Before
    public void setupRepository() {
        LogIt.setStubbedMode(true);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        LegislatorStorage legislatorStorage = new TempLegislatorStorage();
        StubbedLruCache<String, Legislator> cacheMock = new StubbedLruCache<>(String.class, Legislator.class);
        mLegislatorRepository = new LegislatorRepository(legislatorStorage, cacheMock.getMockedCache(), okHttpClient);
    }

    @Test
    public void testFetchByZipCode() {
        ResponseContainer<List<Legislator>> legislatorResponse = mLegislatorRepository.fetchLegislatorListByZip("75001");
        Assert.assertEquals("Response was not successful", ResponseStatus.SUCCESS, legislatorResponse.getResponseStatus());
        Assert.assertTrue("Success evaluation is broken", legislatorResponse.isSuccess());
        Assert.assertTrue("Legislators were not returned", legislatorResponse.getValue().size() > 0);
    }

    @Test
    public void testFetchByCoordinates() {
        Location location = mock(Location.class);
        Mockito.when(location.getLatitude()).thenReturn(32.961790);
        Mockito.when(location.getLongitude()).thenReturn(-96.829169);

        ResponseContainer<List<Legislator>> legislatorResponse = mLegislatorRepository.fetchLegislatorListByCoordinates(location);
        Assert.assertEquals("Response was not successful", ResponseStatus.SUCCESS, legislatorResponse.getResponseStatus());
        Assert.assertTrue("Legislators were not returned", legislatorResponse.getValue().size() > 0);
    }

    @Test
    public void testFetchById() {
        ResponseContainer<Legislator> legislatorResponse = mLegislatorRepository.fetchLegislatorById("S000033");
        Assert.assertEquals("Response was not successful", ResponseStatus.SUCCESS, legislatorResponse.getResponseStatus());
        Assert.assertNotNull("Legislator was returned", legislatorResponse.getValue());
        Assert.assertEquals("Incorrect legislator was returned", "Sanders", legislatorResponse.getValue().getLastName());
    }

    @Test
    public void testStorage() {
        ResponseContainer<List<Legislator>> legislatorResponse = mLegislatorRepository.fetchLegislatorListByZip("75001");
        Assert.assertTrue("Legislators were not returned", legislatorResponse.getValue().size() > 0);
        List<Legislator> originalList = legislatorResponse.getValue();
        for (Legislator legislator: originalList) {
            mLegislatorRepository.addFavoriteLegislator(legislator);
        }

        legislatorResponse = mLegislatorRepository.getFavoriteLegislators();
        Assert.assertTrue("Response was not successful", legislatorResponse.isSuccess());
        Assert.assertTrue("Missing favorites after add", legislatorResponse.getValue().containsAll(originalList) && originalList.containsAll(legislatorResponse.getValue()));

        legislatorResponse = mLegislatorRepository.refreshFavoriteLegislators();
        Assert.assertTrue("Response was not successful", legislatorResponse.isSuccess());
        Assert.assertTrue("Missing favorites after refresh", legislatorResponse.getValue().containsAll(originalList) && originalList.containsAll(legislatorResponse.getValue()));

        Legislator removedLegislator = originalList.get(0);
        mLegislatorRepository.removeFavoriteLegislator(removedLegislator);
        legislatorResponse = mLegislatorRepository.getFavoriteLegislators();
        Assert.assertTrue("Response was not successful", legislatorResponse.isSuccess());
        Assert.assertFalse("Favorite was not removed", legislatorResponse.getValue().contains(removedLegislator));
        Assert.assertTrue("Too many were removed", legislatorResponse.getValue().contains(originalList.get(1)));

        legislatorResponse = mLegislatorRepository.fetchLegislatorListByZip("75001");
        Assert.assertTrue("Favorite flag was dropped on second fetch", legislatorResponse.getValue().get(1).isFavorite());
    }

    @Test
    public void testFetchByName() {
        ResponseContainer<List<Legislator>> legislatorResponse = mLegislatorRepository.fetchLegislatorListByName("Sanders");
        Assert.assertTrue("Response was not successful", legislatorResponse.isSuccess());
        Assert.assertTrue("Missing Bernie after refresh", legislatorResponse.getValue().get(0).getFirstName().equals("Bernard"));
    }

    @Test
    public void testAgent() {
        final ResultContainer<ResponseContainer<List<Legislator>>> testResultContainer = new ResultContainer<>();
        GroundControl.agent(new GetLegislatorsByZipAgent(mLegislatorRepository, "75001"))
                .bgSerialCallback(new FunctionalAgentListener<ResponseContainer<List<Legislator>>, Void>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<List<Legislator>> result) {
                        testResultContainer.setResult(result);
                    }
                }).execute();

        Assert.assertTrue("Timeout occurred", SafeWait.waitOnContainer(testResultContainer));

        Assert.assertTrue("Legislators were not returned", testResultContainer.getResult().getValue().size() > 0);
    }

    private static class TempLegislatorStorage implements LegislatorStorage {
        private Set<String> mFavoriteLegislatorIdSet;

        @Override
        public void saveFavoriteLegislatorIdSet(Set<String> favoriteLegislators) {
            mFavoriteLegislatorIdSet = favoriteLegislators;
        }

        @Override
        public Set<String> loadFavoriteLegislatorIdSet() {
            return mFavoriteLegislatorIdSet;
        }
    }

}
