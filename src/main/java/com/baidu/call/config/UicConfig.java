package com.baidu.call.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Hisenjk on 2017/9/8.
 */

@Configuration
@ImportResource(locations = {"classpath:uic-cxf-${xml.config.classpath}.xml"} )
public class UicConfig {

}