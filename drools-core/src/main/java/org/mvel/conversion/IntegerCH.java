package org.mvel.conversion;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

import org.mvel.ConversionHandler;
import org.mvel.ConversionException;

import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IntegerCH implements ConversionHandler {
    private static final Map<Class, Converter> CNV =
            new HashMap<Class, Converter>(10);


    public Object convertFrom(Object in) {
        if (!CNV.containsKey(in.getClass())) throw new ConversionException("cannot convert type: "
                + in.getClass().getName() + " to: " + Integer.class.getName());

        return CNV.get(in.getClass()).convert(in);
    }


    public boolean canConvertFrom(Class cls) {
        return CNV.containsKey(cls);
    }

    static {
        CNV.put(Object.class,
                new Converter() {
                    public Object convert(Object o) {
                        return parseInt(valueOf(o));
                    }
                }
        );

        CNV.put(BigDecimal.class,
                new Converter() {
                    public Integer convert(Object o) {
                        return ((BigDecimal) o).intValue();
                    }
                }
        );


        CNV.put(BigInteger.class,
                new Converter() {
                    public Integer convert(Object o) {
                        return ((BigInteger) o).intValue();
                    }
                }
        );

        CNV.put(String.class,
                new Converter() {
                    public Object convert(Object o) {
                        return parseInt(((String) o));
                    }
                }
        );

        CNV.put(Short.class,
                new Converter() {
                    public Object convert(Object o) {
                        return ((Short) o).floatValue();
                    }
                }
        );

        CNV.put(Long.class,
                new Converter() {
                    public Object convert(Object o) {
                        //noinspection UnnecessaryBoxing
                        if (((Long) o) > Integer.MAX_VALUE) {
                            throw new ConversionException("cannot coerce Long to Integer since the value ("
                                    + valueOf(o) + ") exceeds that maximum precision of Integer.");
                        }
                        else {
                            return ((Long) o).intValue();
                        }
                    }
                }
        );


        CNV.put(Float.class,
                new Converter() {
                    public Object convert(Object o) {
                        //noinspection UnnecessaryBoxing
                        if (((Float) o) > Integer.MAX_VALUE) {
                            throw new ConversionException("cannot coerce Float to Integer since the value ("
                                    + valueOf(o) + ") exceeds that maximum precision of Integer.");
                        }
                        else {
                            return ((Float) o).intValue();
                        }
                    }
                }
        );

        CNV.put(Double.class,
                new Converter() {
                    public Object convert(Object o) {
                        //noinspection UnnecessaryBoxing
                        if (((Double) o) > Integer.MAX_VALUE) {
                            throw new ConversionException("cannot coerce Long to Integer since the value ("
                                    + valueOf(o) + ") exceeds that maximum precision of Integer.");
                        }
                        else {
                            return ((Double) o).intValue();
                        }
                    }
                }
        );


        CNV.put(Integer.class,
                new Converter() {
                    public Object convert(Object o) {
                        return o;
                    }
                }
        );

        CNV.put(Boolean.class,
                new Converter() {
                    public Integer convert(Object o) {
                        if ((Boolean) o) return 1;
                        else return 0;
                    }
                }
        );


    }


}
