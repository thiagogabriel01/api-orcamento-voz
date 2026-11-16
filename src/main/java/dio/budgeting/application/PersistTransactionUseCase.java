package dio.budgeting.application;

import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class PersistTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "persist-transaction", description = "Persiste uma nova transação financeira")
    public TransactionOutput execute(PersistTransactionInput input) {
        if (input == null || input.description() == null || input.description().isBlank()) {
            throw new InvalidTransactionException("Informe uma descrição para a transação.");
        }
        String description = input.description().strip();
        if (description.codePointCount(0, description.length()) > 255) {
            throw new InvalidTransactionException("A descrição deve ter até 255 caracteres.");
        }
        if (input.amount() <= 0) {
            throw new InvalidTransactionException("Informe um valor positivo em centavos.");
        }
        if (input.category() == null) {
            throw new InvalidTransactionException("Informe uma categoria válida.");
        }
        var transaction = transactionRepository.save(
                new Transaction(description, input.amount(), input.category()));

        return TransactionOutput.from(transaction);
    }
}
