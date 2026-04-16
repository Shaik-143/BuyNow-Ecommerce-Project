package com.buynow.service;

import com.buynow.model.Seller;
import com.buynow.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport( SellerReport sellerReport);

}
