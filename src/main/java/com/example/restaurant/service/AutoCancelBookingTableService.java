package com.example.restaurant.service;

import com.example.restaurant.entity.TableBookingEntity;
import com.example.restaurant.utils.TimeConvertUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AutoCancelBookingTableService {
    @Autowired
    private TableBookingService service;


    @Autowired
    private EmailService emailService;

    @Transactional
    @Scheduled(cron = "0 15 * * * ?") //chạy ở phút 15 mỗi giờ
    public void cancelLateReservations () {
        LocalTime currentTime = LocalTime.now();
        List<TableBookingEntity> tableBookingEntities = service.findLateReservations(currentTime);

        if (tableBookingEntities != null && !tableBookingEntities.isEmpty()) {
            for (TableBookingEntity entity : tableBookingEntities) {
                String date = TimeConvertUtil.convertTimestampToDate(entity.getCreatedAt());
                String mailBody = "Kính gửi " + entity.getCustomer().getName() + ",\n\n" +
                        "Chúng tôi xin gửi lời cảm ơn chân thành đến bạn vì đã lựa chọn dịch vụ của chúng tôi.\n" +
                        "Tuy nhiên, chúng tôi rất tiếc phải thông báo rằng đơn đặt bàn của bạn vào lúc: "
                        + entity.getBookingTime() + " ngày " + date + " đã bị hủy.\n" +
                        "Theo quy định của chúng tôi, nếu khách hàng không đến trong vòng 15 phút sau thời gian đã đặt, lịch đặt bàn sẽ tự động hủy. " +
                        "Chúng tôi rất tiếc vì sự bất tiện này và mong bạn thông cảm.\n\n" +
                        "Nếu bạn muốn đặt lại bàn hoặc có bất kỳ câu hỏi nào, " +
                        "xin vui lòng liên hệ với chúng tôi qua email này hoặc gọi điện thoại đến số 0123456789\n\n" +
                        "Cảm ơn bạn đã hiểu và chúng tôi hy vọng sẽ có cơ hội phục vụ bạn trong tương lai.\n\n"+
                        "Trân trọng,\n" +
                        "Quán nhậu tự do,\n" +
                        "Miếng nào to thì gắp";
                emailService.sendEmail(entity.getCustomer().getEmail(), "Thông báo hủy lịch đặt bàn tại quán nhậu tự do", mailBody);
                entity.setStatus("Đã hủy");
                entity.setNote("Khách không đến đúng giờ!");
                service.save(entity);
            }
        }
    }
}
