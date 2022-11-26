public class Fragment {
    public int size_units;
    public int actually_kb;
    public int head_unit;
    public boolean isFree;
    public Fragment(int head_unit, int uints) {
        this.head_unit = head_unit;
        this.size_units = uints;
        this.actually_kb = 0;
        this.isFree = true;
    }
}
