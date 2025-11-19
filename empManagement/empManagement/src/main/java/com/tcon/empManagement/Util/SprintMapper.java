package com.tcon.empManagement.Util;

import com.tcon.empManagement.Dto.SprintCreateRequest;
import com.tcon.empManagement.Dto.SprintResponse;
import com.tcon.empManagement.Entity.Sprint;

public class SprintMapper {

    public static SprintResponse toResponse(Sprint s) {
        if (s == null) return null;
        SprintResponse r = new SprintResponse();
        r.setId(s.getId());
        r.setProject(s.getProject());
        r.setSprintNumber(s.getSprintNumber());
        r.setYear(s.getYear());
        r.setStartDate(s.getStartDate());
        r.setEndDate(s.getEndDate());
        r.setActive(s.getActive());
        return r;
    }

    public static Sprint toEntity(SprintCreateRequest req) {
        if (req == null) return null;
        Sprint s = new Sprint();
        s.setProject(req.getProject());
        s.setSprintNumber(req.getSprintNumber());
        s.setYear(req.getYear());
        s.setStartDate(req.getStartDate());
        s.setEndDate(req.getEndDate());
        s.setActive(req.getActive());
        return s;
    }
}
