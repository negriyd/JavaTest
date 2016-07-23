package org.javatest.cachemap;

/**
 * Created by negriyd on 23.07.2016.
 */
public class ExpirableValue<ValueType> {
    private ValueType value;
    private long createTime;

    public ExpirableValue(ValueType value) {
        this.value = value;
        this.createTime = Clock.getTime();
    }

    public ValueType getValue() {
        return value;
    }

    public long getCreateTime() {
        return createTime;
    }
}
