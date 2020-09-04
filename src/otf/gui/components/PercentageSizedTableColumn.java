package otf.gui.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author &#8904
 * @param <T>
 *
 */
public class PercentageSizedTableColumn<T> extends TableColumn<T, String>
{
    private final DoubleProperty percentageWidth = new SimpleDoubleProperty(1);

    public PercentageSizedTableColumn()
    {
        tableViewProperty().addListener(new ChangeListener<TableView<T>>()
        {
            @Override
            public void changed(ObservableValue<? extends TableView<T>> observable, TableView<T> oldValue, TableView<T> newValue)
            {
                if (prefWidthProperty().isBound())
                {
                    prefWidthProperty().unbind();
                }

                prefWidthProperty().bind(newValue.widthProperty().multiply(PercentageSizedTableColumn.this.percentageWidth));
            }
        });
    }

    public final DoubleProperty percentageWidthProperty()
    {
        return this.percentageWidth;
    }

    public final double getPercentageWidth()
    {
        return percentageWidthProperty().get();
    }

    public final void setPercentageWidth(double value)
    {
        percentageWidthProperty().set(value / 100);
    }
}