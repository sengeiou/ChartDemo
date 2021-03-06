package com.mobcb.chart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.mobcb.base.util.UnitUtils;
import com.mobcb.chart.ChartConstants;
import com.mobcb.chart.R;
import com.mobcb.chart.bean.DataProperty;

/**
 * ZGQ
 */
public class NormalMarkerView extends MarkerView {

    private TextView mTvChartYValue;
    private TextView mTvChartLineName;
    private TextView mTvChartXValue;
    private IAxisValueFormatter formatter;

    public NormalMarkerView(Context context, int layoutResource, IAxisValueFormatter formatter) {
        super(context, layoutResource);
        this.formatter = formatter;
        mTvChartYValue = (TextView) findViewById(R.id.chart_y_value);
        mTvChartLineName = (TextView) findViewById(R.id.chart_line_name);
        mTvChartXValue = (TextView) findViewById(R.id.chart_x_label);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            mTvChartYValue.setText(String.format("%s",
                    Utils.formatNumber(ce.getHigh(), 0, true, ',')));
        } else {
            mTvChartYValue.setText(String.format("%s",
                    Utils.formatNumber(e.getY(), 0, true, ',')));
        }
        float x = e.getX();
        if (formatter != null) {
            mTvChartXValue.setText(formatter.getFormattedValue(x, null));
        }

        Object data = e.getData();
        if (data != null && data instanceof DataProperty) {
            DataProperty dataProperty = (DataProperty) data;
            mTvChartLineName.setText(String.format(" %s: ", dataProperty.getName()));
            final int color = dataProperty.getColor();
            if (ChartConstants.CHART_TYPE_BARS.equals(dataProperty.getChartType())) {
                Shape shape = new Shape() {
                    @Override
                    public void draw(Canvas canvas, Paint paint) {
                        //设置颜色
                        paint.setColor(color);
                        //设置抗锯齿
                        paint.setAntiAlias(true);
                        //设置画笔粗细
                        paint.setStrokeWidth(1);
                        //设置是否为空心
                        paint.setStyle(Paint.Style.FILL);
                        Rect rect = new Rect(0,
                                0,
                                UnitUtils.dip2px(getContext(), 8),
                                UnitUtils.dip2px(getContext(), 8));
                        canvas.drawRect(rect, paint);
                    }
                };
                shape.resize(UnitUtils.dip2px(getContext(), 8), UnitUtils.dip2px(getContext(), 8));
                ShapeDrawable drawable = new ShapeDrawable(shape);
                drawable.setBounds(0, 0, (int) shape.getWidth(), (int) shape.getHeight());
                mTvChartLineName.setCompoundDrawables(drawable, null, null, null);
                mTvChartLineName.setCompoundDrawablePadding(UnitUtils.dip2px(getContext(), 8));
            } else if (ChartConstants.CHART_TYPE_LINE.equals(dataProperty.getChartType())) {
                Shape shape = new Shape() {
                    @Override
                    public void draw(Canvas canvas, Paint paint) {
                        //设置颜色
                        paint.setColor(color);
                        //设置抗锯齿
                        paint.setAntiAlias(true);
                        //设置画笔粗细
                        paint.setStrokeWidth(1);
                        //设置是否为空心
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawCircle(0, 0, UnitUtils.dip2px(getContext(), 4), paint);
                    }
                };
                shape.resize(UnitUtils.dip2px(getContext(), 5), UnitUtils.dip2px(getContext(), 5));
                ShapeDrawable drawable = new ShapeDrawable(shape);
                drawable.setBounds(UnitUtils.dip2px(getContext(), 4),
                        UnitUtils.dip2px(getContext(), 4),
                        UnitUtils.dip2px(getContext(), 12),
                        UnitUtils.dip2px(getContext(), 12));
                mTvChartLineName.setCompoundDrawables(drawable, null, null, null);
                mTvChartLineName.setCompoundDrawablePadding(UnitUtils.dip2px(getContext(), 6));
            }
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 40);
    }
}