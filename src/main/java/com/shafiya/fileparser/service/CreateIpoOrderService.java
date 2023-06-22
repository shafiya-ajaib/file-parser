package com.shafiya.fileparser.service;

import com.shafiya.fileparser.entity.IpoOrder;

public interface CreateIpoOrderService {
    void saveIfNotExist(IpoOrder ipoOrder);
}
