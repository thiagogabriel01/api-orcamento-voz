package dio.budgeting;

import dio.budgeting.application.InvalidTransactionException;
import dio.budgeting.application.PersistTransactionUseCase;
import dio.budgeting.application.input.PersistTransactionInput;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PersistTransactionUseCaseTest {
    private final TransactionRepository repository = mock(TransactionRepository.class);
    private final PersistTransactionUseCase useCase = new PersistTransactionUseCase(repository);

    @Test
    void rejectsInvalidTransactionsBeforeCallingTheRepository() {
        for (var input : new PersistTransactionInput[]{
                new PersistTransactionInput(" ", 1250, Category.GROCERIES),
                new PersistTransactionInput("x".repeat(256), 1250, Category.GROCERIES),
                new PersistTransactionInput("Mercado", 0, Category.GROCERIES),
                new PersistTransactionInput("Mercado", -1, Category.GROCERIES),
                new PersistTransactionInput("Mercado", 1250, null)
        }) {
            assertThrows(InvalidTransactionException.class, () -> useCase.execute(input));
        }
        verifyNoInteractions(repository);
    }

    @Test
    void storesValidTransactionWithCleanDescription() {
        when(repository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var output = useCase.execute(new PersistTransactionInput("  Mercado  ", 1250, Category.GROCERIES));
        assertEquals("Mercado", output.description());
        assertEquals(1250.0, output.value());
        assertEquals("GROCERIES", output.category());
    }
}
