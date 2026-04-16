package com.buynow.service;

import java.util.List;

import com.buynow.model.Home;
import com.buynow.model.HomeCategory;

public interface HomeService {

    Home creatHomePageData(List<HomeCategory> categories);

}
