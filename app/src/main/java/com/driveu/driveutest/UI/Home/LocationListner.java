package com.driveu.driveutest.UI.Home;

import com.driveu.driveutest.Model.LocationModel;

/**
 * Created by akhil on 30/11/18.
 */

public interface LocationListner {
    void onResponseSuccess(LocationModel locationModel);
    void onResponseFailure(String msg);
    void retrofitError(String msg);
}
