package com.driveu.driveutest.UI.Home;

import com.driveu.driveutest.Model.LocationModel;

/**
 * Created by akhil on 30/11/18.
 */

public class LocationPresenterImpl implements LocationPresenter,LocationListner{
    LocationView locationView;
    LocationInteractor interactor;
    public LocationPresenterImpl(LocationView view){
       this.locationView = view;
       interactor = new LocationInteractorImpl();
    }
    @Override
    public void onResponseSuccess(LocationModel locationModel) {
        locationView.onResponseSuccess(locationModel);
    }

    @Override
    public void onResponseFailure(String msg) {
        locationView.onResponseFailure(msg);
    }

    @Override
    public void retrofitError(String msg) {
        locationView.retrofitError(msg);
    }

    @Override
    public void getLatestLocation() {
        interactor.getLatestLocation(this);
    }
}
