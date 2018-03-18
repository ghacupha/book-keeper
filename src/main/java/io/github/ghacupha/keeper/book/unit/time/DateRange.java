package io.github.ghacupha.keeper.book.unit.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Represents a range timePoints of {@link TimePoint} start, and {@link TimePoint} end
 *
 * adopted from Martin Fowler's Range implementation in {@literal https://martinfowler.com/eaaDev/Range.html}
 * @author edwin.njeru
 */
public class DateRange implements Comparable{

    private static final Logger log = LoggerFactory.getLogger(DateRange.class);

    private TimePoint start;
    private TimePoint end;

    // open ended constructor
    public static DateRange upTo(TimePoint end){
        return new DateRange(TimePoint.PAST,end);
    }

    public static DateRange startingOn(TimePoint start){
        return new DateRange(start,TimePoint.FUTURE);
    }

    // empty DateRange created by providing any end date that is sooner than the start date
    public static DateRange EMPTY = new DateRange(new TimePoint(2000,4,1),
            new TimePoint(2000,1,1));

    public DateRange(TimePoint start, TimePoint end) {
        this.start = start;
        this.end = end;
    }

    public DateRange(LocalDate start, LocalDate end) {
        this.start = new TimePoint(start);
        this.end = new TimePoint(end);
    }

    public TimePoint getEnd() {
        return end;
    }

    public TimePoint getStart() {
        return start;
    }

    public boolean includes(TimePoint arg){
        log.debug("Checking if : {} includes timePoint : {}",this,arg );
        return !arg.before(start) && !arg.after(end);
    }

    private boolean isEmpty(){

        return start.after(end);
    }

    public boolean overlaps(DateRange arg){
        return arg.includes(start) || arg.includes(end) || this.includes(arg);
    }

    public boolean includes(DateRange arg){
        return this.includes(arg.getStart()) && this.includes(arg.getEnd());
    }

    public DateRange gap(DateRange arg){
        if(this.overlaps(arg))
            return DateRange.EMPTY;
        DateRange lower,higher;

        if(this.compareTo(arg) < 0){
            lower = this;
            higher = arg;
        } else {
            lower = arg;
            higher = this;
        }

        return new DateRange(lower.end.addDays(1),higher.start.addDays(-1));
    }

    public boolean abuts(DateRange arg){
        return !this.overlaps(arg)&& this.gap(arg).isEmpty();
    }

    public boolean partitionedBy(DateRange[] args){
        if(! isContiguous(args))
            return false;
        return this.equals(DateRange.combination(args));
    }

    public static DateRange combination(DateRange[] args) {

        Arrays.sort(args);

        if(!isContiguous(args))
            throw new IllegalArgumentException("Unable to combine dateRanges");
        return new DateRange(args[0].start,args[args.length-1].end);
    }

    public static boolean isContiguous(DateRange[] args) {

        Arrays.sort(args);

        for (int i = 0; i < args.length-1; i++) {

            if(! args[i].abuts(args[i+1]))
                return false;
        }

        return true;
    }

    @Override
    public int compareTo(Object arg){
        DateRange other = (DateRange) arg;
        if(! start.equals(other.start))
            return start.compareTo(other.start);
        return end.compareTo(other.end);
    }

    @Override
    public String toString() {
        if(isEmpty())
            return "Empty Date Range";
        return start.toString() + " - "+end.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DateRange))
            return false;
        DateRange other = (DateRange) obj;

        return start.equals(other.getStart()) && end.equals(other.getEnd());
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }
}
