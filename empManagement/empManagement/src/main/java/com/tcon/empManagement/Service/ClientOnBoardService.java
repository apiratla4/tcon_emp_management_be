package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.ClientOnBoardCreateRequest;
import com.tcon.empManagement.Dto.ClientOnBoardResponse;
import com.tcon.empManagement.Dto.ClientOnBoardUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientOnBoardService {
    ClientOnBoardResponse create(ClientOnBoardCreateRequest request);
    ClientOnBoardResponse updateById(String id, ClientOnBoardUpdateRequest request);
    ClientOnBoardResponse getById(String id);
    ClientOnBoardResponse getByProjectId(String projectId);
    Page<ClientOnBoardResponse> list(Pageable pageable);
    List<ClientOnBoardResponse> listByProjectId(String projectId);
    void deleteById(String id);
    long deleteAllByProjectId(String projectId);
}
