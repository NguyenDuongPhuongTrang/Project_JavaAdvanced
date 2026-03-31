package utils;

import java.util.List;

public class TablePrinter {

    public static void printTableWithPagination(
            String title,
            String[] headers,
            List<String[]> rows,
            int pageSize
    ) {
        String headerDisplay = Config.BLUE_BRIGHT + "\n=== " + title + " ===" + Config.RESET;
        int totalRows = rows.size();
        int currentPage = 0;

        while (true) {
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, totalRows);

            System.out.println(headerDisplay);

            // header
            int columnWidth = 28;
            int totalWidth = headers.length * columnWidth;

            // line trên
            for (int i = 10; i < totalWidth; i++) {
                System.out.print("⎯");
            }
            System.out.println();

            // title
            for (String header : headers) {
                System.out.printf("%-" + columnWidth + "s", header);
            }
            System.out.println();

            // line dưới
            for (int i = 10; i < totalWidth; i++) {
                System.out.print("⎯");
            }
            System.out.println();

            // data
            for (int i = start; i < end; i++) {
                String[] row = rows.get(i);
                for (String col : row) {
                    System.out.printf("%-28s", col);
                }
                System.out.println();
            }

            // footer
            int totalPages = (int) Math.ceil((double) totalRows / pageSize);
            System.out.println(Config.PURPLE_BRIGHT + "\nTrang " + (currentPage + 1) + "/" + totalPages + Config.RESET);

            System.out.println(Config.PURPLE_BRIGHT + "[n] Next | [p] Previous | [q] Quit" + Config.RESET);
            String choice = Input.inputString("Chọn: ");

            switch (choice) {
                case "n":
                    if (currentPage < totalPages - 1) currentPage++;
                    else System.out.println(Config.YELLOW + "\nĐã ở trang cuối!" + Config.RESET);
                    break;
                case "p":
                    if (currentPage > 0) currentPage--;
                    else System.out.println(Config.YELLOW + "\nĐã ở trang đầu!" + Config.RESET);
                    break;
                case "q":
                    return;
                default:
                    System.out.println(Config.RED + "\nLựa chọn không hợp lệ!" + Config.RESET);
            }
        }
    }
}