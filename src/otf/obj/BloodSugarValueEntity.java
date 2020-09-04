package otf.obj;

/**
 * @author &#8904
 *
 */
public class BloodSugarValueEntity implements Comparable<BloodSugarValueEntity>
{
    private Long id;
    private long timestamp;
    private int bloodSugar;
    private BolusEntity bolus;

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return this.timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * @return the bloodSugar
     */
    public int getBloodSugar()
    {
        return this.bloodSugar;
    }

    /**
     * @param bloodSugar
     *            the bloodSugar to set
     */
    public void setBloodSugar(int bloodSugar)
    {
        this.bloodSugar = bloodSugar;
    }

    /**
     * @return the bolus
     */
    public BolusEntity getBolus()
    {
        return this.bolus;
    }

    /**
     * @param bolus
     *            the bolus to set
     */
    public void setBolus(BolusEntity bolus)
    {
        this.bolus = bolus;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(BloodSugarValueEntity o)
    {
        int result = 0;

        if (this.timestamp > o.getTimestamp())
        {
            result = -1;
        }
        else if (this.timestamp < o.getTimestamp())
        {
            result = 1;
        }

        return result;
    }
}