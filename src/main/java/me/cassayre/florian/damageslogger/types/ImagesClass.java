package me.cassayre.florian.damageslogger.types;

public enum ImagesClass
{
    MOB("mob"),
    WEAPON("weapon"),
    HEALER("healer");

    private final String CSS_CLASS;

    private ImagesClass(String cssClass)
    {
        CSS_CLASS = cssClass;
    }

    public String getCSSClass()
    {
        return CSS_CLASS;
    }
}
