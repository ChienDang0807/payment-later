package vn.chiendt.skilio.sevice;

import vn.chiendt.skilio.domain.dto.PaylaterPlanCreation;
import vn.chiendt.skilio.domain.dto.PaylaterPlanDto;

public interface PaylaterPlansService {

    /**
     *  Create paylaterPlan
     * @param request
     * @return
     */
    PaylaterPlanDto createPaylaterPlan(PaylaterPlanCreation request);
}
