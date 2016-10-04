package ru.cip.ws.erp.jdbc.entity;

import ru.cip.ws.erp.generated.erptypes.StatusCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 30.09.2016, 2:51 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public enum StatusErp {
    /**
     * Отправили, ждем ответа от СМЭВ
     */
    WAIT,

    /**
     * Отправили корректировку, ждем ответа СМЭВ
     */
    WAIT_FOR_CORRECTION,

    /**
     * Отменили отправку (вручную и на своей стороне)
     */
    CANCELED,

    /**
     * Отказ в приеме заявки в ЕТП
     */
    REJECTED,

    /**
     * Подтверждение приема заявки в ЕТП
     */
    ACCEPTED,

    /**
     * Заявка отправлена в СМЭВ
     */
    SENDED,

    /**
     * Отзка от СМЭВ в обработке заявки
     */
    FAULT,

    /**
     * Ошибка в обработке заявки
     */
    ERROR,

    /**
     * Получен от СМЭВ результат обработки заявки
     */
    SUCCEEDED;

    public static boolean isAnswered(final StatusErp status) {
        return SUCCEEDED.equals(status);
    }

    public static List<StatusErp> incorrectStatuses() {
        final List<StatusErp> result = new ArrayList<>();
        result.add(StatusErp.CANCELED);
        result.add(StatusErp.FAULT);
        result.add(StatusErp.REJECTED);
        result.add(StatusErp.ERROR);
        return result;
    }

    public static StatusErp valueOf(final StatusCode statusCode) {
        switch (statusCode) {
            case REJECTED:
                return StatusErp.REJECTED;
            case ACCEPTED:
                return StatusErp.ACCEPTED;
            case SENDED:
                return StatusErp.SENDED;
            case FAULT:
                return StatusErp.FAULT;
            case ERROR:
                return StatusErp.ERROR;
            case SUCCEEDED:
                return StatusErp.SUCCEEDED;
        }
        return ERROR;
    }
}
