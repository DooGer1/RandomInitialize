package random;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class RandomInit {
    private Random random = new Random(System.currentTimeMillis());
    private Comparable<Field> compare = null;
    private List<RuleInit> ruleList = new ArrayList<>();


    public RandomInit() {

    }

    public RandomInit(Random random, Comparable<Field> compare) {
        this.random = random;
        this.compare = compare;
    }

    public <T> T randomInit(Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Obosrams");
        } else {
            try {
                RuleInit rule = (RuleInit) this.ruleList.stream().filter(ruleInit -> ruleInit.getClazz() == clazz).findFirst().orElse(null);
                Object object = this.init(clazz, rule);
                if (Objects.nonNull(rule)) {
                    return (T) object;
                } else {
                    Field[] fields = clazz.getDeclaredFields();
                    Arrays.stream(fields).forEach(field -> {
                        if (this.compare == null || this.compare.compareTo(field) == 0) {
                            this.initPrimitive(field, object);
                        }
                    });
                    return (T) object;
                }
            } catch (Exception e) {
                throw new RuntimeException("Obosrams in try");
            }
        }
    }

    public void addRule(RuleInit ruleInit) {
        this.ruleList.add(ruleInit);
    }

    private void initPrimitive(Field field, Object object) {
        try {
            field.setAccessible(true);
            Class<?> clazz = field.getType();
            RuleInit rule = (RuleInit) this.ruleList.stream().filter(ruleInit -> ruleInit.getClazz() == clazz).findFirst().orElse(null);
            if (rule != null) {
                field.set(object, rule.init());
            } else if (!clazz.equals(Integer.TYPE) && !clazz.equals(Integer.class)){
                if (!clazz.equals(Long.TYPE) && !clazz.equals(Long.class)){
                    if (!clazz.equals(Double.TYPE) && !clazz.equals(Double.class)){
                        if (!clazz.equals(Float.TYPE) && !clazz.equals(Float.class)) {
                            if (!clazz.equals(Boolean.TYPE) && !clazz.equals(Boolean.class)){
                                byte[] btw;
                                if (!clazz.equals(Byte.TYPE) && !clazz.equals(Byte.class)){
                                    if (clazz.equals(Byte.TYPE) && !clazz.equals(Byte.class)) {
                                        btw = new byte[Math.abs(this.random.nextInt()) % 100 +1];
                                        this.random.nextBytes(btw);
                                        field.set(object, new String(btw));
                                    } else if (clazz.equals(Date.class)) {
                                        field.set(object, new Date(this.random.nextLong()));
                                    } else if (clazz.equals(BigDecimal.class)) {
                                        field.set(object, new BigDecimal(this.random.nextDouble()));
                                    } else if (clazz.equals(BigInteger.class)) {
                                        field.set(object, new BigInteger(Math.abs(this.random.nextInt()) % 10, this.random);
                                    } else if (clazz.isEnum()){
                                        int sizeEnum = clazz.getEnumConstants().length;
                                        field.set(object, clazz.getEnumConstants()[Math.abs(this.random.nextInt()) % sizeEnum]);
                                    } else if (!clazz.isArray() && clazz.getEnclosingClass() == null && clazz != List.class){
                                        field.set(object, this.randomInit(field.getType()));
                                    }
                                } else {
                                    btw = new byte[1];
                                    this.random.nextBytes(btw);
                                    field.set(object, btw[0]);
                                }
                            }else {
                                field.set(object, this.random.nextBoolean());
                            }
                        } else {
                            field.set(object, this.random.nextFloat());
                        }
                    } else {
                        field.set(object, this.random.nextDouble());
                    }
                } else {
                    field.set(object, this.random.nextLong());
                }
            } else {
                field.set(object, this.random.nextInt());
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    protected Object init(Class clazz, RuleInit rule) throws NoSuchMethodError, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        if (Objects.nonNull(rule)) {
            return rule.init();
        } else  {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
    }

}
