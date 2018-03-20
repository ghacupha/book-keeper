package io.github.ghacupha.keeper.book.unit.time;

import java.time.LocalDate;

/**
 * A representation of a point in time to the accuracy of a day
 *
 * @author edwin.njeru
 */
public class TimePoint implements Comparable {

    static final TimePoint PAST = new TimePoint(LocalDate.MIN);
    static final TimePoint FUTURE = new TimePoint(LocalDate.MAX);

    private LocalDate _base;

    private void initialize(LocalDate _arg){
        this._base=_arg;
    }

    public TimePoint(LocalDate arg){
        initialize(arg);
    }

    public TimePoint(int year, int month, int day){
        initialize(LocalDate.of(year,month,day));
    }

    public TimePoint(){
        initialize(LocalDate.now());
    }

    public boolean after(TimePoint arg) {
        //return getDay().after(arg.getDay());
        return getDay().isAfter(arg.getDay());
    }

    public boolean before(TimePoint arg){
        return getDay().isBefore(arg.getDay());
    }

    public TimePoint addDays(int arg){
        return new TimePoint(this._base.plusDays(arg));
    }

    public TimePoint minusDays(int arg){
        return new TimePoint(this._base.minusDays(arg));
    }

    @Override
    public int compareTo(Object arg){
        TimePoint other = (TimePoint) arg;
        return getDay().compareTo(other.getDay());
    }

    @Override
    public boolean equals(Object arg){
       if(! (arg instanceof TimePoint))
           return false;
       TimePoint other = (TimePoint) arg;
       return (_base.equals(other.getDay()));
    }

    @Override
    public int hashCode() {
        return _base != null ? _base.hashCode() : 0;
    }

    @Override
    public String toString() {
        return _base.toString();
    }

    private LocalDate getDay() {
        return _base;
    }
}
