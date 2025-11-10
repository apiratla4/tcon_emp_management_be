package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.ClientOnBoardCreateRequest;
import com.tcon.empManagement.Dto.ClientOnBoardResponse;
import com.tcon.empManagement.Dto.ClientOnBoardUpdateRequest;
import com.tcon.empManagement.Service.ClientOnBoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-onboard")
@CrossOrigin(origins = "*")
public class ClientOnBoardController {

    @Autowired
    private ClientOnBoardService clientOnBoardService;

    @PostMapping
    public ResponseEntity<ClientOnBoardResponse> createClientOnBoard(@Valid @RequestBody ClientOnBoardCreateRequest request) {
        ClientOnBoardResponse response = clientOnBoardService.createClientOnBoard(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientOnBoardResponse> updateClientOnBoard(@PathVariable String id,
                                                                     @RequestBody ClientOnBoardUpdateRequest request) {
        ClientOnBoardResponse response = clientOnBoardService.updateClientOnBoard(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientOnBoardResponse> getClientOnBoardById(@PathVariable String id) {
        ClientOnBoardResponse response = clientOnBoardService.getClientOnBoardById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ClientOnBoardResponse> getClientOnBoardByProjectId(@PathVariable String projectId) {
        ClientOnBoardResponse response = clientOnBoardService.getClientOnBoardByProjectId(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClientOnBoardResponse>> getAllClientOnBoards() {
        List<ClientOnBoardResponse> responses = clientOnBoardService.getAllClientOnBoards();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/business/{businessName}")
    public ResponseEntity<List<ClientOnBoardResponse>> getClientOnBoardsByBusinessName(@PathVariable String businessName) {
        List<ClientOnBoardResponse> responses = clientOnBoardService.getClientOnBoardsByBusinessName(businessName);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/email/{contactEmail}")
    public ResponseEntity<List<ClientOnBoardResponse>> getClientOnBoardsByContactEmail(@PathVariable String contactEmail) {
        List<ClientOnBoardResponse> responses = clientOnBoardService.getClientOnBoardsByContactEmail(contactEmail);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientOnBoard(@PathVariable String id) {
        clientOnBoardService.deleteClientOnBoard(id);
        return ResponseEntity.noContent().build();
    }
}
