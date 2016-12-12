package ru.cip.ws.erp.jpa.entity.enums;

/**
 * Author: Upatov Egor <br>
 * Date: 28.11.2016, 3:39 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description: Список состоний единичной (внеплановой\плановой) проверки
 */
public enum AllocationState {
    /**
     * Ждем ответа от ЕРП на размещение плана проверки
     */
    WAIT_ALLOCATION,
    /**
     * Требуется коррекция плана проверки для его размещения на витрине ЕРП
     */
    PARTIAL_ALLOCATION,
    /**
     * Ошибка при размещении плана проверки
     */
    ERROR_ALLOCATION,
    /**
     * План проверки успешно размещен на витрине ЕРП
     */
    ALLOCATED,

    /**
     * Ждем ответа от ЕРП на размещение результатов плана проверки
     */
    WAIT_RESULT_ALLOCATION,
    /**
     * Требуется коррекция результатов плана проверки для их размещения на витрине ЕРП
     */
    PARTIAL_RESULT_ALLOCATION,
    /**
     *Ошибка при размещении результатов плана проверки
     */
    ERROR_RESULT_ALLOCATION,
    /**
     * Результаты плана проверки успешно размещены на витрине ЕРП
     */
    RESULT_ALLOCATED;
}
