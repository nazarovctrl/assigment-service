package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.file.File;
import uz.ccrew.assignmentservice.entity.User;
import uz.ccrew.assignmentservice.enums.UserRole;
import uz.ccrew.assignmentservice.enums.Category;
import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.enums.TransferType;
import uz.ccrew.assignmentservice.file.FileRepository;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.entity.TransferAssignment;

import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TransferAssignmentRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TransferAssignmentRepository transferAssignmentRepository;

    Assignment createAssignment() {
        File file = File.builder()
                .fileId(UUID.randomUUID())
                .url("http:80/localhost/test/file/url")
                .build();
        fileRepository.save(file);

        User user = User.builder()
                .login("azimjon")
                .role(UserRole.CUSTOMER)
                .password("123")
                .credentialsModifiedDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Assignment assignment = Assignment.builder()
                .fileId(file.getFileId())
                .category(Category.SWIFT_PHYSICAL)
                .details("Details")
                .status(AssignmentStatus.IN_REVIEW)
                .build();
        assignment.setCreatedBy(user);

        assignmentRepository.save(assignment);
        return assignment;
    }

    @Test
    @Transactional
    void saveOk() {
        Assignment assignment = createAssignment();

        TransferAssignment transferAssignment = TransferAssignment.builder()
                .amount(1000L)
                .type(TransferType.SWIFT)
                .receiverCountry("Uzb")
                .receiverFullName("Azimjon")
                .assignment(assignment)
                .build();
        assertDoesNotThrow(() -> transferAssignmentRepository.save(transferAssignment));
    }

    @Test
    @Transactional
    void saveExp() {
        Assignment assignment = createAssignment();

        TransferAssignment transferAssignment = TransferAssignment.builder()
                .amount(1000L)
                .type(TransferType.SWIFT)
                .receiverCountry("Uzb")
                .receiverFullName("Azimjon")
                .phoneNumber("1234123541")
                .assignment(assignment)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            transferAssignmentRepository.save(transferAssignment);
            transferAssignmentRepository.flush();
        });
    }
}