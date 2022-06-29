package fr.pellan.scheduler.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Dto used to store tmp data for the mock controller
 */
@Data
public class MockTestTaskControllerDTO implements Serializable {

    private String testData;
}
