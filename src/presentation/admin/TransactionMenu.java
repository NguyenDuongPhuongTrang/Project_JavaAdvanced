package presentation.admin;

import enums.TransType;
import models.trans.Transaction;
import services.TransactionService;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

public class TransactionMenu {
    TransactionService transactionService = new TransactionService();

    public void showMenu() {
        List<Transaction> list = transactionService.getAll();
        if (list.isEmpty()) {
            System.out.println(Config.YELLOW + "\nLịch sử giao dịch trống!" + Config.RESET);
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (Transaction t : list) {
            String sign = (t.getType() == TransType.DEPOSIT) ? "+" : "-";
            String amountStr = sign + String.format("%.0fđ", t.getAmount());
            rows.add(new String[]{
                    t.getTransactionId(),
                    t.getCustomerId(),
                    t.getType().name(),
                    amountStr,
                    t.getPaymentMethod().name(),
                    t.getDescription(),
                    t.getCreatedAt().toString()
            });
        }

        TablePrinter.printTableWithPagination(
                "LỊCH SỬ GIAO DỊCH",
                new String[]{"ID", "CUSTOMER_ID", "LOẠI GIAO DỊCH", "SỐ TIỀN", "PHƯƠNG THỨC", "MÔ TẢ", "THỜI GIAN"},
                rows,
                5
        );
    }
}
