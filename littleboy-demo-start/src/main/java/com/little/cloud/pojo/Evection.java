package com.little.cloud.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 出差申请中的流程变量对象
 */
@Data
public class Evection implements Serializable {

    /**
     * 主键Id
     */
    private Long id;


    /**
     * 出差单的名字
     */
    private String evectionName;
    /**
     * 出差天数
     */
    private Double num;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 出差结束时间
     */
    private Date endDate;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 出差原因
     */
    private String reson;

}
