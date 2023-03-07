package com.secondproject.coupleaccount.entity;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="expense_import_view")
public class ExpenseImportViewEntity {
     @Id
    @Column(name="member_id") private Long memberId;
    @Column(name="expense_sum") private Integer expenseSum;
    @Column(name="import_sum") private Integer importSum;
    @Column(name="dt") private LocalDate Dt;
}
