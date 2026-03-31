package presentation.admin;

import models.trans.Booking;
import services.BookingService;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

public class BookingMenu {
    private BookingService bookingService = new BookingService();
    public void displayBooking() {
        List<Booking> list = bookingService.getAllBookings();
        if (list.isEmpty()) {
            System.out.println("Không có booking nào!");
            return;
        }

        List<String[]> rows = new ArrayList<>();

        for (Booking b : list) {

            String endTime = (b.getEndTime() != null)
                    ? b.getEndTime().toString()
                    : "Đang chơi";

            rows.add(new String[]{
                    b.getBookingId(),
                    b.getCustomerId(),
                    b.getComputerId(),
                    b.getStartTime().toString(),
                    endTime,
                    b.getStatus()
            });
        }

        TablePrinter.printTableWithPagination(
                "DANH SÁCH BOOKING",
                new String[]{"ID", "CUSTOMER", "MÁY", "BẮT ĐẦU", "KẾT THÚC", "TRẠNG THÁI"},
                rows,
                5
        );
    }
}
