package com.tcon.empManagement.Service;

import com.tcon.empManagement.Entity.AvailableEmpId;
import com.tcon.empManagement.Repository.AvailableEmpIdRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class EmpIdGeneratorService {

    private static final String EMP_ID_PREFIX = "TCONSOL";
    private static final String SEQUENCE_NAME = "empId_sequence";

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private AvailableEmpIdRepository availableEmpIdRepository;

    /**
     * Generates employee ID in format: ARGHSE001, ARGHSE002, etc.
     * Reuses deleted empIds if available.
     */
    public synchronized String generateEmpId() {
        // Check if there's any deleted empId available to reuse
        Optional<AvailableEmpId> availableEmpIdOpt = availableEmpIdRepository.findFirstByOrderByNumericPartAsc();

        if (availableEmpIdOpt.isPresent()) {
            AvailableEmpId availableEmpId = availableEmpIdOpt.get();
            String reusedEmpId = availableEmpId.getEmpId();

            // Delete from available list
            availableEmpIdRepository.delete(availableEmpId);

            log.info("♻️ Reusing deleted empId: {}", reusedEmpId);
            return reusedEmpId;
        }

        // Generate new sequential empId
        long sequence = generateSequence(SEQUENCE_NAME);
        String newEmpId = EMP_ID_PREFIX + String.format("%03d", sequence);
        log.info("✨ Generated new empId: {}", newEmpId);
        return newEmpId;
    }

    /**
     * Mark an empId as available for reuse when employee is deleted
     */
    public void markEmpIdAsAvailable(String empId) {
        if (empId == null || empId.isEmpty()) {
            log.warn("⚠️ Cannot mark empty empId as available");
            return;
        }

        // Check if already exists
        if (availableEmpIdRepository.existsByEmpId(empId)) {
            log.warn("⚠️ EmpId {} already marked as available", empId);
            return;
        }

        // Extract numeric part
        String numericString = empId.replace(EMP_ID_PREFIX, "");
        Long numericPart = Long.parseLong(numericString);

        AvailableEmpId availableEmpId = AvailableEmpId.builder()
                .empId(empId)
                .numericPart(numericPart)
                .build();

        availableEmpIdRepository.save(availableEmpId);
        log.info("♻️ Marked empId as available for reuse: {} (numeric: {})", empId, numericPart);
    }

    /**
     * Generates and increments sequence number
     */
    private synchronized long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );
        return counter != null ? counter.getSeq() : 1;
    }

    @Document(collection = "database_sequences")
    @Data
    public static class DatabaseSequence {
        @Id
        private String id;
        private long seq;
    }
}
