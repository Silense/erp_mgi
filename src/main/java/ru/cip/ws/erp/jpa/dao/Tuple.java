package ru.cip.ws.erp.jpa.dao;

/**
 * Author: Upatov Egor <br>
 * Date: 22.11.2016, 7:36 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:  http://stackoverflow.com/questions/2670982/using-pairs-or-2-tuples-in-java
 */
public class Tuple<X, Y> {
    public final X left;
    public final Y right;
    public Tuple(X left, Y right) {
        this.left = left;
        this.right = right;
    }
}
