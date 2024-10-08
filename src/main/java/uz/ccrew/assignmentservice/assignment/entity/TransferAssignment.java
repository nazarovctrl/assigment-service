package uz.ccrew.assignmentservice.assignment.entity;

import uz.ccrew.assignmentservice.base.Auditable;
import uz.ccrew.assignmentservice.assignment.enums.TransferType;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "transfer_assignments")
@Check(name = "transfer_assignments_c1", constraints = "type = 'SWIFT' and receiver_phone_number is null or type <> 'SWIFT' and receiver_phone_number is not null")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferAssignment extends Auditable {
    @Id
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(nullable = false)
    private String receiverCountry;

    @Enumerated(EnumType.STRING)
    @Column
    private TransferType type;

    @Column
    private String receiverFullName;

    @Column
    private String receiverPhoneNumber;

    @Column(nullable = false)
    private Long amount;

    @MapsId
    @OneToOne
    @JoinColumn(name = "assignment_id", foreignKey = @ForeignKey(name = "transfer_assignments_f1"))
    private Assignment assignment;
}