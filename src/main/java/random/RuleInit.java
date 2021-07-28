package random;

public abstract class RuleInit {

    private Class<T> clazz;

    public RuleInit(Class<T> clazz){this.clazz = clazz}

    public Class<T> getClazz() {return clazz;}

    public abstract T init();
}
