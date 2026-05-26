package com._hills.backend.service;

import com._hills.backend.dto.ExpenseRequest;
import com._hills.backend.entity.Expense;
import com._hills.backend.entity.User;
import com._hills.backend.repository.ExpenseRepository;
import com._hills.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public List<Expense> getAll() {
        return expenseRepository.findAllByOrderByDateDesc();
    }

    public Expense add(String adminEmail, ExpenseRequest req) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Expense expense = Expense.builder()
                .label(req.getLabel())
                .amount(req.getAmount())
                .category(req.getCategory())
                .loggedBy(admin)
                .build();

        return expenseRepository.save(expense);
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }
}
