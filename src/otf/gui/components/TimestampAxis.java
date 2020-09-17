package otf.gui.components;

import java.sql.Timestamp;
import java.util.List;

import javafx.scene.chart.Axis;

/**
 * @author &#8904
 *
 */
public class TimestampAxis extends Axis<Timestamp>
{

    /**
     * @see javafx.scene.chart.Axis#autoRange(double)
     */
    @Override
    protected Object autoRange(double length)
    {
        return null;
    }

    /**
     * @see javafx.scene.chart.Axis#setRange(java.lang.Object, boolean)
     */
    @Override
    protected void setRange(Object range, boolean animate)
    {
    }

    /**
     * @see javafx.scene.chart.Axis#getRange()
     */
    @Override
    protected Object getRange()
    {
        return null;
    }

    /**
     * @see javafx.scene.chart.Axis#getZeroPosition()
     */
    @Override
    public double getZeroPosition()
    {
        return 0;
    }

    /**
     * @see javafx.scene.chart.Axis#getDisplayPosition(java.lang.Object)
     */
    @Override
    public double getDisplayPosition(Timestamp value)
    {
        return 0;
    }

    /**
     * @see javafx.scene.chart.Axis#getValueForDisplay(double)
     */
    @Override
    public Timestamp getValueForDisplay(double displayPosition)
    {
        return null;
    }

    /**
     * @see javafx.scene.chart.Axis#isValueOnAxis(java.lang.Object)
     */
    @Override
    public boolean isValueOnAxis(Timestamp value)
    {
        return false;
    }

    /**
     * @see javafx.scene.chart.Axis#toNumericValue(java.lang.Object)
     */
    @Override
    public double toNumericValue(Timestamp value)
    {
        return 0;
    }

    /**
     * @see javafx.scene.chart.Axis#toRealValue(double)
     */
    @Override
    public Timestamp toRealValue(double value)
    {
        return null;
    }

    /**
     * @see javafx.scene.chart.Axis#calculateTickValues(double, java.lang.Object)
     */
    @Override
    protected List<Timestamp> calculateTickValues(double length, Object range)
    {
        return null;
    }

    /**
     * @see javafx.scene.chart.Axis#getTickMarkLabel(java.lang.Object)
     */
    @Override
    protected String getTickMarkLabel(Timestamp value)
    {
        return null;
    }

}
