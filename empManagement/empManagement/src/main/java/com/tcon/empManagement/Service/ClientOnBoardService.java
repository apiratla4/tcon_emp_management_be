package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.ClientOnBoardCreateRequest;
import com.tcon.empManagement.Dto.ClientOnBoardResponse;
import com.tcon.empManagement.Dto.ClientOnBoardUpdateRequest;

import java.util.List;

public interface ClientOnBoardService {

    ClientOnBoardResponse createClientOnBoard(ClientOnBoardCreateRequest request);

    ClientOnBoardResponse updateClientOnBoard(String id, ClientOnBoardUpdateRequest request);

    ClientOnBoardResponse getClientOnBoardById(String id);

    ClientOnBoardResponse getClientOnBoardByProjectId(String projectId);

    List<ClientOnBoardResponse> getAllClientOnBoards();

    List<ClientOnBoardResponse> getClientOnBoardsByBusinessName(String businessName);

    List<ClientOnBoardResponse> getClientOnBoardsByContactEmail(String contactEmail);

    void deleteClientOnBoard(String id);
}
