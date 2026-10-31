package com.hammatov.MySecondTestAppSpringBoot.service;

import com.hammatov.MySecondTestAppSpringBoot.model.Response;
import com.hammatov.MySecondTestAppSpringBoot.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        String oldSystemTime = response.getSystemTime();

        response.setSystemTime(DateTimeUtil.getCustomFormat().format(new Date()));

        log.info("ModifySystemTimeResponseService: systemTime обновлён: '{}' -> '{}'",
                oldSystemTime, response.getSystemTime());
        return response;
    }

}
