package edu.upi.cs.mfrfauzirahman.imageanalyzer.utilities;

public class ComboItem
{
    private String key;
    private int[] value;

    public ComboItem(String key, int[] value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return key;
    }

    public String getKey()
    {
        return key;
    }

    public int[] getValue()
    {
        return value;
    }
}
